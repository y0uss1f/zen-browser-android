/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.sidebar

import androidx.compose.ui.graphics.Color
import org.mozilla.fenix.sidebar.ZenSidebarResources

enum class SidebarState {
    COLLAPSED,
    EXPANDED,
    HIDDEN,
    NAVIGATING,
}

data class ZenSidebarTab(
    val id: String,
    val title: String,
    val domain: String,
    val placeholderLetter: String = "",
    val faviconDescription: String? = null,
    val faviconColor: Color? = null,
    val isActive: Boolean = false,
    val isPlaying: Boolean = false,
)

data class ZenSidebarUiState(
    val workspaceName: String = "",
    val workspaceIndex: Int = 1,
    val tabs: List<ZenSidebarTab> = emptyList(),
    val selectedTabId: String? = null,
    val state: SidebarState = SidebarState.COLLAPSED,
) {
    val tabCount: Int
        get() = tabs.size

    val activeTab: ZenSidebarTab?
        get() = tabs.firstOrNull { it.id == selectedTabId || it.isActive }
}

