/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.sidebar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ZenSidebarTheme(
    val id: String,
    val displayName: String,

    // Top bar
    val topBarBackground: Color,
    val topBarBorderBottom: Color?,
    val logoColor: Color,
    val urlFieldBackground: Color,
    val urlFieldBorder: Color?,
    val urlFieldTextColor: Color,
    val urlFieldLockColor: Color,
    val urlFieldRadius: Dp,
    val topBarDotsColor: Color,

    // Rail & Sidebar base
    val sidebarBackground: Color,
    val sidebarBorderRight: Color?,
    val workspaceDotBackground: Color,
    val workspaceDotTextColor: Color,
    val workspaceDotRadius: Dp,

    // Expanded panel header
    val workspaceHeaderBackground: Brush,
    val workspaceHeaderTitleColor: Color,
    val workspaceHeaderSubtitleColor: Color,
    val workspaceHeaderBorderBottom: Color?,

    // Tab list
    val tabListBackground: Color,
    val tabItemDividerColor: Color?,
    val activeTabBackground: Color,
    val activeTabTextColor: Color,
    val inactiveTabTextColor: Color,
    val activeTabSubtitleColor: Color,
    val inactiveTabSubtitleColor: Color,
    val tabCloseColor: Color,
    val tabCloseActiveColor: Color,
    val activeTabRadius: Dp,
    val activeTabBorderLeft: Color?,
    val activeTabOutlineColor: Color?,

    // Rail elements
    val railFaviconRadius: Dp,
    val railFaviconBorder: Color?,
    val railButtonBackground: Color,
    val railButtonContentColor: Color,
    val railButtonRadius: Dp,
    val railButtonBorderColor: Color?,

    // Expanded panel bottom
    val panelBottomBackground: Color,
    val newTabButtonBackground: Color,
    val newTabButtonContentColor: Color,
    val hideSidebarButtonBackground: Color,
    val hideSidebarButtonContentColor: Color,
    val panelButtonRadius: Dp,
    val panelButtonBorderColor: Color?,

    // Edge strip
    val edgeStripBackground: Color,
    val edgeStripGripColor: Color,
    val edgeStripBorderRight: Color?,

    // Bottom bar
    val bottomBarBackground: Color,
    val bottomBarBorderTop: Color?,
    val bottomBarIconColor: Color,
    val grabberIconColor: Color,

    // Typography & Visual effects
    val fontFamily: FontFamily = FontFamily.Default,
    val isUppercase: Boolean = false,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val isBeveled: Boolean = false,
    val bevelHighlightColor: Color = Color(0xFFFFFFFF),
    val bevelShadowColor: Color = Color(0xFF404040),
    val hasShadows: Boolean = false,
) {
    companion object {
        val Midnight = ZenSidebarTheme(
            id = "dark",
            displayName = "Midnight UI",
            topBarBackground = Color(0xFF1C1C1E),
            topBarBorderBottom = Color(0xFF2C2C2E),
            logoColor = Color(0xFFFF7F00),
            urlFieldBackground = Color(0xFF2C2C2E),
            urlFieldBorder = Color(0xFF3A3A3C),
            urlFieldTextColor = Color(0xFFE5E5EA),
            urlFieldLockColor = Color(0xFF8E8E93),
            urlFieldRadius = 8.dp,
            topBarDotsColor = Color(0xFFFFFFFF),
            sidebarBackground = Color(0xFF1C1C1E),
            sidebarBorderRight = Color(0xFF2C2C2E),
            workspaceDotBackground = Color(0xFF2C2C2E),
            workspaceDotTextColor = Color(0xFFFFFFFF),
            workspaceDotRadius = 4.dp,
            workspaceHeaderBackground = SolidColor(Color(0xFF2C2C2E)),
            workspaceHeaderTitleColor = Color(0xFFFFFFFF),
            workspaceHeaderSubtitleColor = Color(0xFF8E8E93),
            workspaceHeaderBorderBottom = Color(0xFF3A3A3C),
            tabListBackground = Color(0xFF121212),
            tabItemDividerColor = Color(0xFF2C2C2E),
            activeTabBackground = Color(0xFF2C2C2E),
            activeTabTextColor = Color(0xFFFFFFFF),
            inactiveTabTextColor = Color(0xFFE5E5EA),
            activeTabSubtitleColor = Color(0xFF98989D),
            inactiveTabSubtitleColor = Color(0xFF636366),
            tabCloseColor = Color(0xFF8E8E93),
            tabCloseActiveColor = Color(0xFFE5E5EA),
            activeTabRadius = 8.dp,
            activeTabBorderLeft = null,
            activeTabOutlineColor = Color(0xFF0A84FF),
            railFaviconRadius = 4.dp,
            railFaviconBorder = null,
            railButtonBackground = Color(0xFF2C2C2E),
            railButtonContentColor = Color(0xFFFFFFFF),
            railButtonRadius = 4.dp,
            railButtonBorderColor = Color(0xFF3A3A3C),
            panelBottomBackground = Color(0xFF1C1C1E),
            newTabButtonBackground = Color(0xFF0A84FF),
            newTabButtonContentColor = Color(0xFFFFFFFF),
            hideSidebarButtonBackground = Color(0xFF2C2C2E),
            hideSidebarButtonContentColor = Color(0xFFFFFFFF),
            panelButtonRadius = 8.dp,
            panelButtonBorderColor = null,
            edgeStripBackground = Color(0xFF1C1C1E),
            edgeStripGripColor = Color(0xFF8E8E93),
            edgeStripBorderRight = Color(0xFF2C2C2E),
            bottomBarBackground = Color(0xFF1C1C1E),
            bottomBarBorderTop = Color(0xFF2C2C2E),
            bottomBarIconColor = Color(0xFFFFFFFF),
            grabberIconColor = Color(0xFFFF7F00),
        )

        val Retro = ZenSidebarTheme(
            id = "retro",
            displayName = "Retro UI",
            topBarBackground = Color(0xFFD4D0C8),
            topBarBorderBottom = Color(0xFF808080),
            logoColor = Color(0xFF000000),
            urlFieldBackground = Color(0xFFFFFFFF),
            urlFieldBorder = Color(0xFF808080),
            urlFieldTextColor = Color(0xFF000000),
            urlFieldLockColor = Color(0xFF555555),
            urlFieldRadius = 2.dp,
            topBarDotsColor = Color(0xFF000000),
            sidebarBackground = Color(0xFFD4D0C8),
            sidebarBorderRight = Color(0xFF808080),
            workspaceDotBackground = Color(0xFF000000),
            workspaceDotTextColor = Color(0xFFFFFFFF),
            workspaceDotRadius = 2.dp,
            workspaceHeaderBackground = Brush.horizontalGradient(
                listOf(Color(0xFF000080), Color(0xFF1084D0)),
            ),
            workspaceHeaderTitleColor = Color(0xFFFFFFFF),
            workspaceHeaderSubtitleColor = Color(0xFFD0D0FF),
            workspaceHeaderBorderBottom = null,
            tabListBackground = Color(0xFFFFFFFF),
            tabItemDividerColor = Color(0xFFC0C0C0),
            activeTabBackground = Color(0xFF000080),
            activeTabTextColor = Color(0xFFFFFFFF),
            inactiveTabTextColor = Color(0xFF000000),
            activeTabSubtitleColor = Color(0xFFC0C0FF),
            inactiveTabSubtitleColor = Color(0xFF555555),
            tabCloseColor = Color(0xFF808080),
            tabCloseActiveColor = Color(0xFFFFFFFF),
            activeTabRadius = 0.dp,
            activeTabBorderLeft = null,
            activeTabOutlineColor = Color(0xFF000080),
            railFaviconRadius = 2.dp,
            railFaviconBorder = Color(0xFF000000),
            railButtonBackground = Color(0xFFD4D0C8),
            railButtonContentColor = Color(0xFF000000),
            railButtonRadius = 2.dp,
            railButtonBorderColor = Color(0xFF808080),
            panelBottomBackground = Color(0xFFD4D0C8),
            newTabButtonBackground = Color(0xFF000080),
            newTabButtonContentColor = Color(0xFFFFFFFF),
            hideSidebarButtonBackground = Color(0xFFD4D0C8),
            hideSidebarButtonContentColor = Color(0xFF000000),
            panelButtonRadius = 2.dp,
            panelButtonBorderColor = Color(0xFF404040),
            edgeStripBackground = Color(0xFFD4D0C8),
            edgeStripGripColor = Color(0xFF000000),
            edgeStripBorderRight = Color(0xFF808080),
            bottomBarBackground = Color(0xFFD4D0C8),
            bottomBarBorderTop = Color(0xFFFFFFFF),
            bottomBarIconColor = Color(0xFF000000),
            grabberIconColor = Color(0xFF000000),
            fontFamily = FontFamily.Monospace,
            isBeveled = true,
            bevelHighlightColor = Color(0xFFFFFFFF),
            bevelShadowColor = Color(0xFF404040),
        )

        // Working name "Nothing" (name TBD)
        val Nothing = ZenSidebarTheme(
            id = "nothing",
            displayName = "Nothing",
            topBarBackground = Color(0xFF000000),
            topBarBorderBottom = Color(0xFFFFFFFF),
            logoColor = Color(0xFFFFFFFF),
            urlFieldBackground = Color(0xFF000000),
            urlFieldBorder = Color(0xFFFFFFFF),
            urlFieldTextColor = Color(0xFFFFFFFF),
            urlFieldLockColor = Color(0xFFD5001C),
            urlFieldRadius = 0.dp,
            topBarDotsColor = Color(0xFFFFFFFF),
            sidebarBackground = Color(0xFF000000),
            sidebarBorderRight = Color(0xFFFFFFFF),
            workspaceDotBackground = Color(0xFFD5001C),
            workspaceDotTextColor = Color(0xFFFFFFFF),
            workspaceDotRadius = 0.dp,
            workspaceHeaderBackground = SolidColor(Color(0xFF000000)),
            workspaceHeaderTitleColor = Color(0xFFFFFFFF),
            workspaceHeaderSubtitleColor = Color(0xFF999999),
            workspaceHeaderBorderBottom = Color(0xFFFFFFFF),
            tabListBackground = Color(0xFF000000),
            tabItemDividerColor = Color(0xFF333333),
            activeTabBackground = Color(0xFF111111),
            activeTabTextColor = Color(0xFFFFFFFF),
            inactiveTabTextColor = Color(0xFFFFFFFF),
            activeTabSubtitleColor = Color(0xFFCCCCCC),
            inactiveTabSubtitleColor = Color(0xFF999999),
            tabCloseColor = Color(0xFF999999),
            tabCloseActiveColor = Color(0xFFFFFFFF),
            activeTabRadius = 0.dp,
            activeTabBorderLeft = Color(0xFFD5001C),
            activeTabOutlineColor = Color(0xFFD5001C),
            railFaviconRadius = 0.dp,
            railFaviconBorder = Color(0xFFFFFFFF),
            railButtonBackground = Color(0xFF000000),
            railButtonContentColor = Color(0xFFFFFFFF),
            railButtonRadius = 0.dp,
            railButtonBorderColor = Color(0xFFFFFFFF),
            panelBottomBackground = Color(0xFF000000),
            newTabButtonBackground = Color(0xFFD5001C),
            newTabButtonContentColor = Color(0xFFFFFFFF),
            hideSidebarButtonBackground = Color(0xFF000000),
            hideSidebarButtonContentColor = Color(0xFFFFFFFF),
            panelButtonRadius = 0.dp,
            panelButtonBorderColor = Color(0xFFFFFFFF),
            edgeStripBackground = Color(0xFF000000),
            edgeStripGripColor = Color(0xFFFFFFFF),
            edgeStripBorderRight = Color(0xFFFFFFFF),
            bottomBarBackground = Color(0xFF000000),
            bottomBarBorderTop = Color(0xFFFFFFFF),
            bottomBarIconColor = Color(0xFFFFFFFF),
            grabberIconColor = Color(0xFFD5001C),
            fontFamily = FontFamily.Monospace,
            isUppercase = true,
            letterSpacing = 2.sp,
        )

        val Softglass = ZenSidebarTheme(
            id = "softglass",
            displayName = "Softglass UI",
            topBarBackground = Color(0xFFE9E9EE),
            topBarBorderBottom = null,
            logoColor = Color(0xFF23233A),
            urlFieldBackground = Color(0xFFE9E9EE),
            urlFieldBorder = null,
            urlFieldTextColor = Color(0xFF555555),
            urlFieldLockColor = Color(0xFF999999),
            urlFieldRadius = 13.dp,
            topBarDotsColor = Color(0xFF666666),
            sidebarBackground = Color(0xFFE9E9EE),
            sidebarBorderRight = null,
            workspaceDotBackground = Color(0xFF23233A),
            workspaceDotTextColor = Color(0xFFFFFFFF),
            workspaceDotRadius = 8.dp,
            workspaceHeaderBackground = SolidColor(Color(0xE023233A)),
            workspaceHeaderTitleColor = Color(0xFFFFFFFF),
            workspaceHeaderSubtitleColor = Color(0xFFBBBBBB),
            workspaceHeaderBorderBottom = null,
            tabListBackground = Color(0xFFE9E9EE),
            tabItemDividerColor = Color(0xFFDDDDDD),
            activeTabBackground = Color(0xFFECECF1),
            activeTabTextColor = Color(0xFF333333),
            inactiveTabTextColor = Color(0xFF333333),
            activeTabSubtitleColor = Color(0xFF777777),
            inactiveTabSubtitleColor = Color(0xFF999999),
            tabCloseColor = Color(0xFF999999),
            tabCloseActiveColor = Color(0xFF555555),
            activeTabRadius = 12.dp,
            activeTabBorderLeft = null,
            activeTabOutlineColor = null,
            railFaviconRadius = 8.dp,
            railFaviconBorder = null,
            railButtonBackground = Color(0xFFE9E9EE),
            railButtonContentColor = Color(0xFF555555),
            railButtonRadius = 13.dp,
            railButtonBorderColor = null,
            panelBottomBackground = Color(0xFFE9E9EE),
            newTabButtonBackground = Color(0xFF23233A),
            newTabButtonContentColor = Color(0xFFFFFFFF),
            hideSidebarButtonBackground = Color(0xFFE9E9EE),
            hideSidebarButtonContentColor = Color(0xFF555555),
            panelButtonRadius = 12.dp,
            panelButtonBorderColor = null,
            edgeStripBackground = Color(0xFFE9E9EE),
            edgeStripGripColor = Color(0xFF999999),
            edgeStripBorderRight = null,
            bottomBarBackground = Color(0xFFE9E9EE),
            bottomBarBorderTop = null,
            bottomBarIconColor = Color(0xFF555555),
            grabberIconColor = Color(0xFFD43D3D),
            hasShadows = true,
        )


        val Natureglass = ZenSidebarTheme(
            id = "natureglass",
            displayName = "Natureglass UI",
            topBarBackground = Color(0xB81A1A2E),
            topBarBorderBottom = Color(0xFF33334D),
            logoColor = Color(0xFFFF8C1A),
            urlFieldBackground = Color(0xFF2A2A40),
            urlFieldBorder = Color(0xFF3A3A5E),
            urlFieldTextColor = Color(0xFFE8E8F0),
            urlFieldLockColor = Color(0xFF6A5CFF),
            urlFieldRadius = 2.dp,
            topBarDotsColor = Color(0xFFFFFFFF),
            sidebarBackground = Color(0xFF1A1A2E),
            sidebarBorderRight = Color(0xFF33334D),
            workspaceDotBackground = Color(0xFF6A5CFF),
            workspaceDotTextColor = Color(0xFFFFFFFF),
            workspaceDotRadius = 2.dp,
            workspaceHeaderBackground = Brush.horizontalGradient(
                listOf(Color(0xFF1E1E3F), Color(0xFF2D2D6A)),
            ),
            workspaceHeaderTitleColor = Color(0xFFFFFFFF),
            workspaceHeaderSubtitleColor = Color(0xFFD0D0FF),
            workspaceHeaderBorderBottom = Color(0xFF33334D),
            tabListBackground = Color(0xFF1A1A2E),
            tabItemDividerColor = Color(0xFF33334D),
            activeTabBackground = Color(0xFF2A2A40),
            activeTabTextColor = Color(0xFFFFFFFF),
            inactiveTabTextColor = Color(0xFFE8E8F0),
            activeTabSubtitleColor = Color(0xFF9999BB),
            inactiveTabSubtitleColor = Color(0xFF9999BB),
            tabCloseColor = Color(0xFF9999BB),
            tabCloseActiveColor = Color(0xFFFFFFFF),
            activeTabRadius = 2.dp,
            activeTabBorderLeft = null,
            activeTabOutlineColor = null,
            railFaviconRadius = 2.dp,
            railFaviconBorder = null,
            railButtonBackground = Color(0xFF2A2A40),
            railButtonContentColor = Color(0xFFFFFFFF),
            railButtonRadius = 2.dp,
            railButtonBorderColor = null,
            panelBottomBackground = Color(0xFF1A1A2E),
            newTabButtonBackground = Color(0xFF6A5CFF),
            newTabButtonContentColor = Color(0xFFFFFFFF),
            hideSidebarButtonBackground = Color(0xFF2A2A40),
            hideSidebarButtonContentColor = Color(0xFFE8E8F0),
            panelButtonRadius = 2.dp,
            panelButtonBorderColor = null,
            edgeStripBackground = Color(0xFF1A1A2E),
            edgeStripGripColor = Color(0xFFFFFFFF),
            edgeStripBorderRight = Color(0xFF33334D),
            bottomBarBackground = Color(0xB81A1A2E),
            bottomBarBorderTop = Color(0xFF33334D),
            bottomBarIconColor = Color(0xFFFFFFFF),
            grabberIconColor = Color(0xFF6A5CFF),
        )

        val builtins: List<ZenSidebarTheme> = listOf(
            Natureglass,
            Retro,
            Midnight,
            Softglass,
            Nothing,
        )

        val Default: ZenSidebarTheme = Natureglass
    }
}

val LocalZenSidebarTheme = staticCompositionLocalOf { ZenSidebarTheme.Default }

@Composable
fun ZenSidebarThemeProvider(
    theme: ZenSidebarTheme = ZenSidebarTheme.Default,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalZenSidebarTheme provides theme) {
        content()
    }
}

fun Modifier.retroBevel(
    highlightColor: Color = Color(0xFFFFFFFF),
    shadowColor: Color = Color(0xFF404040),
    strokeWidth: Dp = 2.dp,
): Modifier = this.drawBehind {
    val strokePx = strokeWidth.toPx()
    val w = size.width
    val h = size.height
    drawLine(highlightColor, Offset(0f, strokePx / 2), Offset(w, strokePx / 2), strokePx)
    drawLine(highlightColor, Offset(strokePx / 2, 0f), Offset(strokePx / 2, h), strokePx)
    drawLine(shadowColor, Offset(0f, h - strokePx / 2), Offset(w, h - strokePx / 2), strokePx)
    drawLine(shadowColor, Offset(w - strokePx / 2, 0f), Offset(w - strokePx / 2, h), strokePx)
}

fun Modifier.retroInsetBevel(
    shadowColor: Color = Color(0xFF808080),
    highlightColor: Color = Color(0xFFFFFFFF),
    strokeWidth: Dp = 2.dp,
): Modifier = this.drawBehind {
    val strokePx = strokeWidth.toPx()
    val w = size.width
    val h = size.height
    drawLine(shadowColor, Offset(0f, strokePx / 2), Offset(w, strokePx / 2), strokePx)
    drawLine(shadowColor, Offset(strokePx / 2, 0f), Offset(strokePx / 2, h), strokePx)
    drawLine(highlightColor, Offset(0f, h - strokePx / 2), Offset(w, h - strokePx / 2), strokePx)
    drawLine(highlightColor, Offset(w - strokePx / 2, 0f), Offset(w - strokePx / 2, h), strokePx)
}
