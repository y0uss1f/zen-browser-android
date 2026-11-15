/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.sidebar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

private const val BACK_SVG = "M15 5l-7 7 7 7"
private const val GRABBER_CLOUD_SVG = "M18 10h-1.26A8 8 0 1 0 9 20h9a5 5 0 0 0 0-10z"
private const val GRABBER_STEM_SVG = "M12 9v7.5"
private const val GRABBER_HEAD_SVG = "M8.8 13.3L12 16.5l3.2-3.2"
private const val SEARCH_HANDLE_SVG = "M16.5 16.5L21 21"
private const val DOWNLOADS_STEM_SVG = "M12 3v11"
private const val DOWNLOADS_HEAD_SVG = "M6.5 10.5L12 16l5.5-5.5"
private const val DOWNLOADS_TRAY_SVG = "M4 20h16"
private const val HISTORY_HANDS_SVG = "M12 7.5V12l3 2"

private inline fun DrawScope.drawIn24Units(block: DrawScope.() -> Unit) {
    val scale = size.minDimension / 24f
    withTransform({
        scale(scale, scale, pivot = Offset.Zero)
    }) {
        block()
    }
}

@Composable
fun ZenBottomBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onGrabber: () -> Unit = {},
    onSearch: () -> Unit = {},
    onDownloads: () -> Unit = {},
    onHistory: () -> Unit = {},
) {
    val theme = LocalZenSidebarTheme.current

    val backPath = remember { PathParser().parsePathString(BACK_SVG).toPath() }
    val grabberCloudPath = remember { PathParser().parsePathString(GRABBER_CLOUD_SVG).toPath() }
    val grabberStemPath = remember { PathParser().parsePathString(GRABBER_STEM_SVG).toPath() }
    val grabberHeadPath = remember { PathParser().parsePathString(GRABBER_HEAD_SVG).toPath() }
    val searchHandlePath = remember { PathParser().parsePathString(SEARCH_HANDLE_SVG).toPath() }
    val downloadsStemPath = remember { PathParser().parsePathString(DOWNLOADS_STEM_SVG).toPath() }
    val downloadsHeadPath = remember { PathParser().parsePathString(DOWNLOADS_HEAD_SVG).toPath() }
    val downloadsTrayPath = remember { PathParser().parsePathString(DOWNLOADS_TRAY_SVG).toPath() }
    val historyHandsPath = remember { PathParser().parsePathString(HISTORY_HANDS_SVG).toPath() }

    val topBorderModifier = if (theme.bottomBarBorderTop != null) {
        val borderWidth = if (theme.isBeveled) 2.dp else 1.dp
        Modifier.drawBehind {
            val strokePx = borderWidth.toPx()
            drawLine(
                color = theme.bottomBarBorderTop,
                start = Offset(0f, strokePx / 2f),
                end = Offset(size.width, strokePx / 2f),
                strokeWidth = strokePx,
            )
        }
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(theme.bottomBarBackground)
            .then(topBorderModifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        // Back
        ZenBottomBarItem(
            description = stringResource(ZenSidebarResources.BACK_DESCRIPTION),
            onClick = onBack,
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                drawIn24Units {
                    val stroke = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    drawPath(backPath, theme.bottomBarIconColor, style = stroke)
                }
            }
        }

        // Grabber
        ZenBottomBarItem(
            description = stringResource(ZenSidebarResources.GRABBER_DESCRIPTION),
            onClick = onGrabber,
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                drawIn24Units {
                    val stroke = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    drawPath(grabberCloudPath, theme.grabberIconColor, style = stroke)
                    drawPath(grabberStemPath, theme.grabberIconColor, style = stroke)
                    drawPath(grabberHeadPath, theme.grabberIconColor, style = stroke)
                }
            }
        }

        // Search
        ZenBottomBarItem(
            description = stringResource(ZenSidebarResources.SEARCH_DESCRIPTION),
            onClick = onSearch,
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                drawIn24Units {
                    val stroke = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    drawCircle(
                        color = theme.bottomBarIconColor,
                        radius = 7f,
                        center = Offset(11f, 11f),
                        style = stroke,
                    )
                    drawPath(searchHandlePath, theme.bottomBarIconColor, style = stroke)
                }
            }
        }

        // Downloads
        ZenBottomBarItem(
            description = stringResource(ZenSidebarResources.DOWNLOADS_DESCRIPTION),
            onClick = onDownloads,
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                drawIn24Units {
                    val stroke = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    drawPath(downloadsStemPath, theme.bottomBarIconColor, style = stroke)
                    drawPath(downloadsHeadPath, theme.bottomBarIconColor, style = stroke)
                    drawPath(downloadsTrayPath, theme.bottomBarIconColor, style = stroke)
                }
            }
        }

        // History
        ZenBottomBarItem(
            description = stringResource(ZenSidebarResources.HISTORY_DESCRIPTION),
            onClick = onHistory,
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                drawIn24Units {
                    val stroke = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    drawCircle(
                        color = theme.bottomBarIconColor,
                        radius = 8.5f,
                        center = Offset(12f, 12f),
                        style = stroke,
                    )
                    drawPath(historyHandsPath, theme.bottomBarIconColor, style = stroke)
                }
            }
        }
    }
}

@Composable
private fun ZenBottomBarItem(
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clickable(
                role = Role.Button,
                onClickLabel = description,
                onClick = onClick,
            )
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
