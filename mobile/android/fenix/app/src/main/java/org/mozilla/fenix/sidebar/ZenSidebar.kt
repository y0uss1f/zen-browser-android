/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.sidebar

import android.content.Context
import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs

val ZEN_SIDEBAR_COLLAPSED_WIDTH = 32.dp
val ZEN_SIDEBAR_EXPANDED_WIDTH = 272.dp
val ZEN_SIDEBAR_EDGE_STRIP_WIDTH = 7.dp
val ZEN_SIDEBAR_DRAG_HANDLE_WIDTH = 12.dp

private fun rubberband(overshoot: Float, dimension: Float, constant: Float = 0.55f): Float {
    return (overshoot * dimension * constant) / (dimension + constant * abs(overshoot))
}

private fun projectEndpoint(
    currentPosition: Float,
    initialVelocity: Float,
    decelerationRate: Float = 0.998f,
): Float {
    return currentPosition + (initialVelocity / 1000f) * decelerationRate / (1f - decelerationRate)
}

private fun isReducedMotion(context: Context): Boolean {
    return try {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f,
        ) == 0f
    } catch (_: Exception) {
        false
    }
}

@Composable
fun ZenEdgeStrip(
    modifier: Modifier = Modifier,
    onEdgeStripTap: () -> Unit = {},
) {
    val theme = LocalZenSidebarTheme.current
    val edgeStripDescription = stringResource(ZenSidebarResources.EDGE_STRIP_DESCRIPTION)

    val rightBorderModifier = if (theme.edgeStripBorderRight != null) {
        val borderWidth = if (theme.isBeveled) 2.dp else 1.dp
        Modifier.drawBehind {
            val strokePx = borderWidth.toPx()
            drawLine(
                color = theme.edgeStripBorderRight,
                start = Offset(size.width - strokePx / 2f, 0f),
                end = Offset(size.width - strokePx / 2f, size.height),
                strokeWidth = strokePx,
            )
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .width(ZEN_SIDEBAR_EDGE_STRIP_WIDTH)
            .fillMaxHeight()
            .background(theme.edgeStripBackground)
            .then(rightBorderModifier)
            .clickable(
                role = Role.Button,
                onClickLabel = edgeStripDescription,
                onClick = onEdgeStripTap,
            )
            .semantics { contentDescription = edgeStripDescription },
        contentAlignment = Alignment.CenterStart,
    ) {
        val gripRadius = if (theme.urlFieldRadius == 0.dp) 0.dp else 2.dp
        Box(
            modifier = Modifier
                .padding(start = 1.dp)
                .size(width = 3.dp, height = 34.dp)
                .clip(RoundedCornerShape(gripRadius))
                .background(theme.edgeStripGripColor),
        )
    }
}

@Composable
fun ZenSidebar(
    uiState: ZenSidebarUiState,
    modifier: Modifier = Modifier,
    onTabSelected: (String) -> Unit = {},
    onTabClosed: (String) -> Unit = {},
    onNewTab: () -> Unit = {},
    onHide: () -> Unit = {},
    onExpand: () -> Unit = {},
    onCollapse: () -> Unit = {},
    onEdgeStripTap: () -> Unit = {},
    onNavigateHome: () -> Unit = {},
    onWidthChanged: ((Dp) -> Unit)? = null,
) {
    if (uiState.state == SidebarState.HIDDEN || uiState.state == SidebarState.NAVIGATING) {
        return
    }

    val theme = LocalZenSidebarTheme.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val collapsedPx = with(density) { ZEN_SIDEBAR_COLLAPSED_WIDTH.toPx() }
    val expandedPx = with(density) { ZEN_SIDEBAR_EXPANDED_WIDTH.toPx() }
    val hysteresisPx = with(density) { 10.dp.toPx() }

    val initialWidth = if (uiState.state == SidebarState.EXPANDED) expandedPx else collapsedPx
    val widthAnimatable = remember { Animatable(initialWidth) }
    var dragAnimationJob by remember { mutableStateOf<Job?>(null) }
    val velocityTracker = remember { VelocityTracker() }

    LaunchedEffect(uiState.state) {
        val target = if (uiState.state == SidebarState.EXPANDED) expandedPx else collapsedPx
        if (widthAnimatable.targetValue != target) {
            if (isReducedMotion(context)) {
                widthAnimatable.snapTo(target)
            } else {
                widthAnimatable.animateTo(
                    targetValue = target,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                )
            }
        }
    }

    LaunchedEffect(widthAnimatable.value) {
        onWidthChanged?.invoke(with(density) { widthAnimatable.value.toDp() })
    }

    val currentWidthDp = with(density) { widthAnimatable.value.coerceAtLeast(0f).toDp() }
    val isExpandedMode = widthAnimatable.value > (collapsedPx + expandedPx) / 2f

    val rightBorderModifier = if (theme.sidebarBorderRight != null) {
        val borderWidth = if (theme.isBeveled) 2.dp else 1.dp
        Modifier.drawBehind {
            val strokePx = borderWidth.toPx()
            drawLine(
                color = theme.sidebarBorderRight,
                start = Offset(size.width - strokePx / 2f, 0f),
                end = Offset(size.width - strokePx / 2f, size.height),
                strokeWidth = strokePx,
            )
        }
    } else {
        Modifier
    }

    val shadowModifier = if (theme.hasShadows) {
        Modifier.shadow(elevation = 6.dp, shape = RectangleShape)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .width(currentWidthDp)
            .fillMaxHeight()
            .then(shadowModifier)
            .background(theme.sidebarBackground)
            .then(rightBorderModifier)
            .clipToBounds()
            .clickable(
                enabled = !isExpandedMode,
                onClick = onExpand,
            ),
    ) {
        if (isExpandedMode) {
            ZenSidebarExpandedPanel(
                uiState = uiState,
                theme = theme,
                onTabSelected = onTabSelected,
                onTabClosed = onTabClosed,
                onNewTab = onNewTab,
                onHide = onHide,
                onCollapse = onCollapse,
            )
        } else {
            ZenSidebarCollapsedRail(
                uiState = uiState,
                theme = theme,
                onTabSelected = onTabSelected,
                onNewTab = onNewTab,
                onHide = onHide,
            )
        }

        // Drag handle (12dp wide on the right edge)
        Box(
            modifier = Modifier
                .width(ZEN_SIDEBAR_DRAG_HANDLE_WIDTH)
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        dragAnimationJob?.cancel()
                        coroutineScope.launch { widthAnimatable.stop() }
                        velocityTracker.resetTracking()
                        velocityTracker.addPosition(down.uptimeMillis, down.position)

                        val startWidth = widthAnimatable.value
                        var totalDeltaX = 0f
                        var isDragging = false

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            if (!change.pressed) {
                                if (isDragging) {
                                    val velocity = velocityTracker.calculateVelocity().x
                                    val currentWidth = widthAnimatable.value
                                    val projected = projectEndpoint(currentWidth, velocity)
                                    val midpoint = (collapsedPx + expandedPx) / 2f
                                    val target = if (projected >= midpoint) expandedPx else collapsedPx
                                    val isFlick = abs(velocity) > 200f
                                    val damping = if (isFlick) 0.8f else Spring.DampingRatioNoBouncy

                                    dragAnimationJob = coroutineScope.launch {
                                        if (isReducedMotion(context)) {
                                            widthAnimatable.snapTo(target)
                                        } else {
                                            widthAnimatable.animateTo(
                                                targetValue = target,
                                                animationSpec = spring(
                                                    dampingRatio = damping,
                                                    stiffness = Spring.StiffnessMediumLow,
                                                ),
                                                initialVelocity = velocity,
                                            )
                                        }
                                        if (target == expandedPx) {
                                            onExpand()
                                        } else {
                                            onCollapse()
                                        }
                                    }
                                }
                                break
                            }

                            val delta = change.position.x - change.previousPosition.x
                            totalDeltaX += delta
                            velocityTracker.addPosition(change.uptimeMillis, change.position)

                            if (!isDragging) {
                                if (abs(totalDeltaX) >= hysteresisPx) {
                                    isDragging = true
                                    change.consume()
                                }
                            }

                            if (isDragging) {
                                change.consume()
                                val rawWidth = startWidth + totalDeltaX
                                val resistedWidth = when {
                                    rawWidth > expandedPx -> {
                                        expandedPx + rubberband(rawWidth - expandedPx, dimension = expandedPx)
                                    }
                                    rawWidth < collapsedPx -> {
                                        collapsedPx + rubberband(rawWidth - collapsedPx, dimension = collapsedPx)
                                    }
                                    else -> rawWidth
                                }
                                coroutineScope.launch {
                                    widthAnimatable.snapTo(resistedWidth)
                                }
                            }
                        }
                    }
                },
        )
    }
}

@Composable
private fun ZenSidebarCollapsedRail(
    uiState: ZenSidebarUiState,
    theme: ZenSidebarTheme,
    onTabSelected: (String) -> Unit,
    onNewTab: () -> Unit,
    onHide: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(ZEN_SIDEBAR_COLLAPSED_WIDTH)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Rail Top: Workspace "1" square (22dp)
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .size(22.dp)
                .clip(RoundedCornerShape(theme.workspaceDotRadius))
                .background(theme.workspaceDotBackground),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = uiState.workspaceIndex.toString(),
                color = theme.workspaceDotTextColor,
                fontSize = 11.sp,
                fontFamily = theme.fontFamily,
                fontWeight = FontWeight.ExtraBold,
            )
        }

        // Rail Tabs: Tab favicons (22dp each, active outlined)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
        ) {
            items(uiState.tabs, key = { it.id }) { tab ->
                val isActive = tab.id == uiState.selectedTabId || tab.isActive
                ZenRailFavicon(
                    tab = tab,
                    isActive = isActive,
                    theme = theme,
                    onClick = { onTabSelected(tab.id) },
                )
            }
        }

        // Rail Bottom: "+" (new tab) and "‹" (hide) buttons (26dp each)
        Column(
            modifier = Modifier
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ZenRailButton(
                text = stringResource(ZenSidebarResources.NEW_TAB_PLUS),
                theme = theme,
                onClick = onNewTab,
            )

            ZenRailButton(
                text = stringResource(ZenSidebarResources.COLLAPSE_CHEVRON),
                theme = theme,
                onClick = onHide,
            )
        }
    }
}

@Composable
private fun ZenRailFavicon(
    tab: ZenSidebarTab,
    isActive: Boolean,
    theme: ZenSidebarTheme,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(theme.railFaviconRadius)
    val outlineModifier = if (isActive && theme.activeTabOutlineColor != null) {
        Modifier.border(2.dp, theme.activeTabOutlineColor, shape)
    } else if (theme.railFaviconBorder != null) {
        Modifier.border(1.dp, theme.railFaviconBorder, shape)
    } else {
        Modifier
    }

    val backgroundColor = tab.faviconColor ?: when (tab.domain.lowercase()) {
        "youtube.com" -> Color(0xFFFF0000)
        "x.com" -> Color(0xFF000000)
        "instagram.com" -> Color(0xFFE1306C)
        "en.wikipedia.org", "wikipedia.org" -> Color(0xFFFFFFFF)
        "github.com" -> Color(0xFF24292F)
        "google.com" -> Color(0xFF4285F4)
        else -> theme.activeTabBackground
    }

    val contentColor = if (backgroundColor == Color(0xFFFFFFFF)) Color(0xFF111111) else Color(0xFFFFFFFF)
    val letter = if (tab.placeholderLetter.isNotEmpty()) {
        tab.placeholderLetter
    } else {
        tab.domain.firstOrNull()?.uppercase() ?: "?"
    }

    Box(
        modifier = Modifier
            .size(22.dp)
            .clickable(
                role = Role.Tab,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(shape)
                .background(backgroundColor)
                .then(outlineModifier),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = letter,
                color = contentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        }

        if (tab.isPlaying) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5484D))
                    .border(1.dp, theme.sidebarBackground, CircleShape),
            )
        }
    }
}

@Composable
private fun ZenRailButton(
    text: String,
    theme: ZenSidebarTheme,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(theme.railButtonRadius)
    val borderModifier = when {
        theme.isBeveled -> Modifier.retroBevel()
        theme.railButtonBorderColor != null -> Modifier.border(1.dp, theme.railButtonBorderColor, shape)
        else -> Modifier
    }

    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(shape)
            .background(theme.railButtonBackground)
            .then(borderModifier)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = theme.railButtonContentColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ZenSidebarExpandedPanel(
    uiState: ZenSidebarUiState,
    theme: ZenSidebarTheme,
    onTabSelected: (String) -> Unit,
    onTabClosed: (String) -> Unit,
    onNewTab: () -> Unit,
    onHide: () -> Unit,
    onCollapse: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(ZEN_SIDEBAR_EXPANDED_WIDTH)
            .fillMaxHeight(),
    ) {
        // Workspace Header
        val headerBorderModifier = if (theme.workspaceHeaderBorderBottom != null) {
            Modifier.drawBehind {
                val strokePx = 1.dp.toPx()
                drawLine(
                    color = theme.workspaceHeaderBorderBottom,
                    start = Offset(0f, size.height - strokePx / 2f),
                    end = Offset(size.width, size.height - strokePx / 2f),
                    strokeWidth = strokePx,
                )
            }
        } else {
            Modifier
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.workspaceHeaderBackground)
                .then(headerBorderModifier)
                .padding(horizontal = 10.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = uiState.workspaceName,
                    color = theme.workspaceHeaderTitleColor,
                    fontSize = 15.sp,
                    fontFamily = theme.fontFamily,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = theme.letterSpacing,
                )

                Text(
                    text = stringResource(ZenSidebarResources.HIDE_CHEVRON),
                    color = theme.workspaceHeaderTitleColor,
                    fontSize = 13.sp,
                    fontFamily = theme.fontFamily,
                    modifier = Modifier
                        .clickable(
                            role = Role.Button,
                            onClick = onCollapse,
                        )
                        .padding(4.dp),
                )
            }

            val subtitleText = if (uiState.tabs.size == 1) {
                String.format(stringResource(ZenSidebarResources.WORKSPACE_SUBTITLE_FORMAT_SINGLE), 1)
            } else {
                String.format(stringResource(ZenSidebarResources.WORKSPACE_SUBTITLE_FORMAT), uiState.tabs.size)
            }

            Text(
                text = subtitleText,
                color = theme.workspaceHeaderSubtitleColor,
                fontSize = 11.sp,
                fontFamily = theme.fontFamily,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        // Tab list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(theme.tabListBackground)
                .padding(vertical = 4.dp),
        ) {
            items(uiState.tabs, key = { it.id }) { tab ->
                val isActive = tab.id == uiState.selectedTabId || tab.isActive
                ZenExpandedTabRow(
                    tab = tab,
                    isActive = isActive,
                    theme = theme,
                    onSelect = { onTabSelected(tab.id) },
                    onClose = { onTabClosed(tab.id) },
                )
            }
        }

        // Panel Bottom: "+ New tab" and "Hide sidebar"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.panelBottomBackground)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ZenPanelButton(
                text = stringResource(ZenSidebarResources.NEW_TAB),
                backgroundColor = theme.newTabButtonBackground,
                contentColor = theme.newTabButtonContentColor,
                theme = theme,
                modifier = Modifier.weight(1f),
                onClick = onNewTab,
            )

            ZenPanelButton(
                text = stringResource(ZenSidebarResources.HIDE_SIDEBAR),
                backgroundColor = theme.hideSidebarButtonBackground,
                contentColor = theme.hideSidebarButtonContentColor,
                theme = theme,
                modifier = Modifier.weight(1f),
                onClick = onHide,
            )
        }
    }
}

@Composable
private fun ZenExpandedTabRow(
    tab: ZenSidebarTab,
    isActive: Boolean,
    theme: ZenSidebarTheme,
    onSelect: () -> Unit,
    onClose: () -> Unit,
) {
    val rowShape = RoundedCornerShape(if (isActive) theme.activeTabRadius else 0.dp)
    val rowBackground = if (isActive) theme.activeTabBackground else Color.Transparent

    val dividerModifier = if (!isActive && theme.tabItemDividerColor != null) {
        Modifier.drawBehind {
            val strokePx = 1.dp.toPx()
            drawLine(
                color = theme.tabItemDividerColor,
                start = Offset(0f, size.height - strokePx / 2f),
                end = Offset(size.width, size.height - strokePx / 2f),
                strokeWidth = strokePx,
            )
        }
    } else {
        Modifier
    }

    val activeBorderLeftModifier = if (isActive && theme.activeTabBorderLeft != null) {
        Modifier.drawBehind {
            val strokePx = 3.dp.toPx()
            drawLine(
                color = theme.activeTabBorderLeft,
                start = Offset(strokePx / 2f, 0f),
                end = Offset(strokePx / 2f, size.height),
                strokeWidth = strokePx,
            )
        }
    } else {
        Modifier
    }

    val shadowModifier = if (isActive && theme.hasShadows) {
        Modifier.shadow(elevation = 4.dp, shape = rowShape)
    } else {
        Modifier
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isActive && theme.activeTabRadius > 0.dp) Modifier.padding(horizontal = 8.dp, vertical = 3.dp) else Modifier)
            .then(shadowModifier)
            .clip(rowShape)
            .background(rowBackground)
            .then(dividerModifier)
            .then(activeBorderLeftModifier)
            .clickable(
                role = Role.Tab,
                onClick = onSelect,
            )
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Tab Favicon (30dp)
        val faviconShape = RoundedCornerShape(theme.railFaviconRadius)
        val backgroundColor = tab.faviconColor ?: when (tab.domain.lowercase()) {
            "youtube.com" -> Color(0xFFFF0000)
            "x.com" -> Color(0xFF000000)
            "instagram.com" -> Color(0xFFE1306C)
            "en.wikipedia.org", "wikipedia.org" -> Color(0xFFFFFFFF)
            "github.com" -> Color(0xFF24292F)
            "google.com" -> Color(0xFF4285F4)
            else -> theme.activeTabBackground
        }
        val contentColor = if (backgroundColor == Color(0xFFFFFFFF)) Color(0xFF111111) else Color(0xFFFFFFFF)
        val letter = if (tab.placeholderLetter.isNotEmpty()) {
            tab.placeholderLetter
        } else {
            tab.domain.firstOrNull()?.uppercase() ?: "?"
        }

        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(faviconShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = letter,
                    color = contentColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            if (tab.isPlaying) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE5484D))
                        .border(1.dp, theme.sidebarBackground, CircleShape),
                )
            }
        }

        // Title and Domain
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = tab.title,
                color = if (isActive) theme.activeTabTextColor else theme.inactiveTabTextColor,
                fontSize = 13.sp,
                fontFamily = theme.fontFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = tab.domain,
                color = if (isActive) theme.activeTabSubtitleColor else theme.inactiveTabSubtitleColor,
                fontSize = 11.sp,
                fontFamily = theme.fontFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 1.dp),
            )
        }

        // Close button "×"
        Text(
            text = stringResource(ZenSidebarResources.CLOSE_TAB),
            color = if (isActive) theme.tabCloseActiveColor else theme.tabCloseColor,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable(
                    role = Role.Button,
                    onClick = onClose,
                )
                .padding(4.dp),
        )
    }
}

@Composable
private fun ZenPanelButton(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    theme: ZenSidebarTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(theme.panelButtonRadius)
    val borderModifier = when {
        theme.isBeveled -> Modifier.retroBevel()
        theme.panelButtonBorderColor != null -> Modifier.border(1.dp, theme.panelButtonBorderColor, shape)
        else -> Modifier
    }

    Box(
        modifier = modifier
            .height(38.dp)
            .clip(shape)
            .background(backgroundColor)
            .then(borderModifier)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 13.sp,
            fontFamily = theme.fontFamily,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = theme.letterSpacing,
        )
    }
}
