/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.browser

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import mozilla.components.browser.state.selector.selectedTab
import mozilla.components.browser.state.state.BrowserState
import mozilla.components.browser.state.state.TabSessionState
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.concept.engine.mediasession.MediaSession
import mozilla.components.lib.state.ext.observeAsComposableState
import org.mozilla.fenix.components.components
import org.mozilla.fenix.dejavu.menu.readOnMainThread
import org.mozilla.fenix.dejavu.settings.DejavuSettings
import org.mozilla.fenix.dejavu.workspaces.WorkspaceRepository
import org.mozilla.fenix.sidebar.SidebarState
import org.mozilla.fenix.sidebar.ZenBottomBar
import org.mozilla.fenix.sidebar.ZenEdgeStrip
import org.mozilla.fenix.sidebar.ZenSidebar
import org.mozilla.fenix.sidebar.ZenSidebarResources
import org.mozilla.fenix.sidebar.ZenSidebarTab
import org.mozilla.fenix.sidebar.ZenSidebarTheme
import org.mozilla.fenix.sidebar.ZenSidebarThemeProvider
import org.mozilla.fenix.sidebar.ZenSidebarUiState
import org.mozilla.fenix.sidebar.ZenTopBar

private val ZEN_NAVIGATION_EXIT_BAND = 40.dp

@Composable
internal fun ZenChromeTheme(content: @Composable () -> Unit) {
    ZenSidebarThemeProvider(theme = rememberZenTheme(), content = content)
}

@Composable
private fun rememberZenTheme(): ZenSidebarTheme {
    val context = LocalContext.current
    val components = components
    val settings = remember { components.readOnMainThread { DejavuSettings.get(context) } }
    val themeId by settings.sidebarThemeId.collectAsState()
    return ZenSidebarTheme.builtins.firstOrNull { it.id == themeId } ?: ZenSidebarTheme.Default
}

@Composable
internal fun ZenTopBarContent(
    store: BrowserStore,
    onLogoClick: () -> Unit,
    onUrlClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    val tab by store.observeAsComposableState { it.selectedTab }
    ZenTopBar(
        url = tab?.content?.url.orEmpty(),
        isSecure = tab?.content?.securityInfo?.isSecure == true,
        onLogoClick = onLogoClick,
        onUrlClick = onUrlClick,
        onMenuClick = onMenuClick,
    )
}

@Composable
internal fun ZenSidebarContent(
    store: BrowserStore,
    state: SidebarState,
    onTabSelected: (String) -> Unit,
    onTabClosed: (String) -> Unit,
    onNewTab: () -> Unit,
    onHide: () -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onEdgeStripTap: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    when (state) {
        SidebarState.NAVIGATING -> return
        SidebarState.HIDDEN -> {
            ZenEdgeStrip(onEdgeStripTap = onEdgeStripTap)
            return
        }
        else -> Unit
    }

    val context = LocalContext.current
    val components = components
    val workspaces = remember { components.readOnMainThread { WorkspaceRepository.get(context) } }
    val workspaceState by workspaces.state.collectAsState()
    val defaultWorkspaceName = stringResource(ZenSidebarResources.DEFAULT_WORKSPACE_NAME)
    val browserState by store.observeAsComposableState { it }

    ZenSidebar(
        uiState = browserState.toZenSidebarUiState(
            workspaceName = workspaceState.activeWorkspace?.name ?: defaultWorkspaceName,
            workspaceIndex = workspaceState.activeIndex + 1,
            state = state,
        ),
        onTabSelected = onTabSelected,
        onTabClosed = onTabClosed,
        onNewTab = onNewTab,
        onHide = onHide,
        onExpand = onExpand,
        onCollapse = onCollapse,
        onEdgeStripTap = onEdgeStripTap,
        onNavigateHome = onNavigateHome,
    )
}

@Composable
internal fun ZenBottomBarContent(
    onBack: () -> Unit,
    onGrabber: () -> Unit,
    onSearch: () -> Unit,
    onDownloads: () -> Unit,
    onHistory: () -> Unit,
) {
    ZenBottomBar(
        onBack = onBack,
        onGrabber = onGrabber,
        onSearch = onSearch,
        onDownloads = onDownloads,
        onHistory = onHistory,
    )
}

@Composable
internal fun ZenNavigateTopContent(onExit: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ZEN_NAVIGATION_EXIT_BAND)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onExit,
            ),
    )
}

@Composable
internal fun ZenNavigateStripContent(onExit: () -> Unit) {
    ZenEdgeStrip(onEdgeStripTap = onExit)
}

private fun BrowserState.toZenSidebarUiState(
    workspaceName: String,
    workspaceIndex: Int,
    state: SidebarState,
): ZenSidebarUiState = ZenSidebarUiState(
    workspaceName = workspaceName,
    workspaceIndex = workspaceIndex,
    tabs = tabs.map { it.toZenSidebarTab(selectedTabId) },
    selectedTabId = selectedTabId,
    state = state,
)

private fun TabSessionState.toZenSidebarTab(selectedTabId: String?): ZenSidebarTab {
    val domain = content.url.hostOrEmpty()
    return ZenSidebarTab(
        id = id,
        title = content.title.ifBlank { content.url }.ifBlank { domain },
        domain = domain,
        placeholderLetter = domain.firstOrNull()?.uppercase() ?: "?",
        isActive = id == selectedTabId,
        isPlaying = mediaSessionState?.playbackState == MediaSession.PlaybackState.PLAYING,
    )
}

private fun String.hostOrEmpty(): String = Uri.parse(this).host.orEmpty()
