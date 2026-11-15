/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val sampleTabs = listOf(
    ZenSidebarTab(
        id = "tab-0",
        title = "Lofi hip hop radio — beats to relax/study to",
        domain = "youtube.com",
        placeholderLetter = "▶",
        faviconColor = Color(0xFFFF0000),
        isActive = true,
    ),
    ZenSidebarTab(
        id = "tab-1",
        title = "Home / X",
        domain = "x.com",
        placeholderLetter = "𝕏",
        faviconColor = Color(0xFF000000),
    ),
    ZenSidebarTab(
        id = "tab-2",
        title = "Instagram",
        domain = "instagram.com",
        placeholderLetter = "◎",
        faviconColor = Color(0xFFE1306C),
    ),
    ZenSidebarTab(
        id = "tab-3",
        title = "Zen (browser) — Wikipedia",
        domain = "en.wikipedia.org",
        placeholderLetter = "W",
        faviconColor = Color(0xFFFFFFFF),
    ),
    ZenSidebarTab(
        id = "tab-4",
        title = "y0uss1f / zen-browser-android",
        domain = "github.com",
        placeholderLetter = "⬢",
        faviconColor = Color(0xFF24292F),
    ),
    ZenSidebarTab(
        id = "tab-5",
        title = "Google",
        domain = "google.com",
        placeholderLetter = "G",
        faviconColor = Color(0xFF4285F4),
    ),
)

@Composable
private fun MockWebPageContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(18.dp),
    ) {
        Text(
            text = "Zen browser is coming to Android — and it is open source",
            color = Color(0xFF111111),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
        )

        Text(
            text = "theverge.com · 2 hours ago",
            color = Color(0xFF888888),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF6A5CFF), Color(0xFF00C6A2)),
                    ),
                )
                .padding(12.dp),
            contentAlignment = Alignment.BottomStart,
        ) {
            Text(
                text = "A browser that puts your tabs on the side",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Text(
            text = "Zen has built a loyal following on desktop for one idea: the browser should get out of the way. Tabs live in a slim sidebar on the left, workspaces keep projects apart, and the page gets the whole screen.",
            color = Color(0xFF444444),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

@Composable
private fun ZenBrowserFrame(
    theme: ZenSidebarTheme,
    state: SidebarState,
) {
    val uiState = ZenSidebarUiState(
        workspaceName = "Workspace 1",
        workspaceIndex = 1,
        tabs = sampleTabs,
        selectedTabId = "tab-0",
        state = state,
    )

    ZenSidebarThemeProvider(theme = theme) {
        Column(
            modifier = Modifier
                .size(width = 360.dp, height = 720.dp)
                .background(Color(0xFFE5E5E5)),
        ) {
            if (state != SidebarState.NAVIGATING) {
                ZenTopBar(
                    url = "https://theverge.com",
                    isSecure = true,
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    when (state) {
                        SidebarState.COLLAPSED, SidebarState.EXPANDED -> {
                            ZenSidebar(uiState = uiState)
                        }
                        SidebarState.HIDDEN, SidebarState.NAVIGATING -> {
                            ZenEdgeStrip()
                        }
                    }

                    MockWebPageContent(modifier = Modifier.weight(1f))
                }

                if (state == SidebarState.NAVIGATING) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFFE082))
                            .border(1.dp, Color(0xFF8D6E63), RoundedCornerShape(4.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    ) {
                        Text(
                            text = stringResource(ZenSidebarResources.NAVIGATING_HINT),
                            color = Color(0xFF3E2723),
                            fontSize = 11.sp,
                        )
                    }
                }
            }

            if (state != SidebarState.NAVIGATING) {
                ZenBottomBar()
            }
        }
    }
}

// 8 Required Previews (4 states x 2 themes: Midnight and Retro)

@Preview(name = "1. Midnight - Collapsed Rail", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewCollapsedMidnight() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Midnight,
        state = SidebarState.COLLAPSED,
    )
}

@Preview(name = "2. Midnight - Expanded Panel", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewExpandedMidnight() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Midnight,
        state = SidebarState.EXPANDED,
    )
}

@Preview(name = "3. Midnight - Hidden (Edge Strip)", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewHiddenMidnight() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Midnight,
        state = SidebarState.HIDDEN,
    )
}

@Preview(name = "4. Midnight - Navigating (Fullscreen)", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewNavigatingMidnight() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Midnight,
        state = SidebarState.NAVIGATING,
    )
}

@Preview(name = "5. Retro - Collapsed Rail", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewCollapsedRetro() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Retro,
        state = SidebarState.COLLAPSED,
    )
}

@Preview(name = "6. Retro - Expanded Panel", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewExpandedRetro() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Retro,
        state = SidebarState.EXPANDED,
    )
}

@Preview(name = "7. Retro - Hidden (Edge Strip)", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewHiddenRetro() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Retro,
        state = SidebarState.HIDDEN,
    )
}

@Preview(name = "8. Retro - Navigating (Fullscreen)", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewNavigatingRetro() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Retro,
        state = SidebarState.NAVIGATING,
    )
}

// Additional Theme Previews for Full Builtin Registry Eyeballing

@Preview(name = "9. Nothing - Expanded Panel", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewExpandedNothing() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Nothing,
        state = SidebarState.EXPANDED,
    )
}

@Preview(name = "10. Softglass - Expanded Panel", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewExpandedSoftglass() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Softglass,
        state = SidebarState.EXPANDED,
    )
}

@Preview(name = "11. Natureglass - Expanded Panel", widthDp = 360, heightDp = 720)
@Composable
fun ZenSidebarPreviewExpandedNatureglass() {
    ZenBrowserFrame(
        theme = ZenSidebarTheme.Natureglass,
        state = SidebarState.EXPANDED,
    )
}

@Preview(name = "12. Top Bar & Bottom Bar - Midnight", widthDp = 360)
@Composable
fun ZenBarsPreviewMidnight() {
    ZenSidebarThemeProvider(theme = ZenSidebarTheme.Midnight) {
        Column {
            ZenTopBar()
            Spacer(modifier = Modifier.height(16.dp))
            ZenBottomBar()
        }
    }
}
