/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.sidebar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ZenRingsLogo(
    modifier: Modifier = Modifier,
    color: Color = LocalZenSidebarTheme.current.logoColor,
) {
    Canvas(modifier = modifier.size(22.dp)) {
        val scale = size.minDimension / 100f
        val strokeWidth = 9f * scale
        val strokeRound = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        val strokePlain = Stroke(width = strokeWidth)

        val arcRadius = 42f * scale
        val centerOffset = Offset(50f * scale, 50f * scale)
        val arcTopLeft = Offset(centerOffset.x - arcRadius, centerOffset.y - arcRadius)
        val arcSize = Size(arcRadius * 2f, arcRadius * 2f)

        // Outer ring with a 70-degree gap at the top (-55° to -125°)
        drawArc(
            color = color,
            startAngle = -55f,
            sweepAngle = 290f,
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = strokeRound,
        )

        // Middle ring
        drawCircle(
            color = color,
            radius = 27f * scale,
            center = centerOffset,
            style = strokePlain,
        )

        // Inner ring
        drawCircle(
            color = color,
            radius = 12f * scale,
            center = centerOffset,
            style = strokePlain,
        )

        // Center dot
        drawCircle(
            color = color,
            radius = 4.5f * scale,
            center = centerOffset,
        )
    }
}

@Composable
fun ZenTopBar(
    modifier: Modifier = Modifier,
    url: String = "",
    isSecure: Boolean = true,
    onLogoClick: () -> Unit = {},
    onUrlClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) {
    val theme = LocalZenSidebarTheme.current
    val logoDescription = stringResource(ZenSidebarResources.LOGO_DESCRIPTION)
    val menuDescription = stringResource(ZenSidebarResources.MENU_DESCRIPTION)
    val httpsScheme = stringResource(ZenSidebarResources.HTTPS_SCHEME)
    val searchOrEnterAddress = stringResource(ZenSidebarResources.SEARCH_OR_ENTER_ADDRESS)

    val bottomBorderModifier = if (theme.topBarBorderBottom != null) {
        val borderHeight = if (theme.isBeveled) 2.dp else 1.dp
        Modifier.drawBehind {
            val strokePx = borderHeight.toPx()
            drawLine(
                color = theme.topBarBorderBottom,
                start = Offset(0f, size.height - strokePx / 2f),
                end = Offset(size.width, size.height - strokePx / 2f),
                strokeWidth = strokePx,
            )
        }
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(theme.topBarBackground)
            .then(bottomBorderModifier)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        // Left: Zen rings logo (22dp)
        Box(
            modifier = Modifier
                .size(22.dp)
                .clickable(
                    role = Role.Button,
                    onClickLabel = logoDescription,
                    onClick = onLogoClick,
                )
                .semantics { contentDescription = logoDescription },
            contentAlignment = Alignment.Center,
        ) {
            ZenRingsLogo(color = theme.logoColor)
        }

        // Center: URL Field
        val shape = RoundedCornerShape(theme.urlFieldRadius)
        val urlFieldBorderModifier = when {
            theme.isBeveled -> Modifier.retroInsetBevel()
            theme.urlFieldBorder != null -> Modifier.border(1.dp, theme.urlFieldBorder, shape)
            else -> Modifier
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .height(26.dp)
                .clip(shape)
                .background(theme.urlFieldBackground)
                .then(urlFieldBorderModifier)
                .clickable(
                    role = Role.Button,
                    onClick = onUrlClick,
                )
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (isSecure) {
                Text(
                    text = httpsScheme,
                    color = theme.urlFieldLockColor,
                    fontSize = 11.sp,
                    fontFamily = theme.fontFamily,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = theme.letterSpacing,
                )
            }

            Text(
                text = url.ifEmpty { searchOrEnterAddress },
                color = theme.urlFieldTextColor,
                fontSize = 12.sp,
                fontFamily = theme.fontFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = theme.letterSpacing,
            )
        }

        // Right: vertical dots (three 4dp dots stacked)
        Box(
            modifier = Modifier
                .size(22.dp)
                .clickable(
                    role = Role.Button,
                    onClickLabel = menuDescription,
                    onClick = onMenuClick,
                )
                .semantics { contentDescription = menuDescription },
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(theme.topBarDotsColor),
                    )
                }
            }
        }
    }
}
