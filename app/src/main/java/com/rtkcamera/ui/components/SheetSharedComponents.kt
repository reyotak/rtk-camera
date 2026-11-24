package com.rtkcamera.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtkcamera.ui.icons.RtkIcons
import com.rtkcamera.ui.theme.RtkColorTokens

/**
 * Shared dark frosted bottom sheet shell matching Hi-Fi 02, 05, 06, and 07.
 */
@Composable
fun RtkBottomSheetContainer(
    title: String,
    subtitle: String? = null,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(RtkColorTokens.ScrimSheetBackdrop)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = RtkColorTokens.SurfaceSheet,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            border = BorderStroke(1.5.dp, RtkColorTokens.BorderSheet),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(width = 40.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(RtkColorTokens.TextMuted)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = RtkColorTokens.TextPrimary
                            )
                        )
                        if (subtitle != null) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = RtkColorTokens.TextMuted
                                )
                            )
                        }
                    }

                    // 48x48dp touch target around 44dp close button
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = RtkColorTokens.SurfaceChipIdle,
                            shape = CircleShape,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = RtkIcons.Close,
                                    contentDescription = "Close",
                                    tint = RtkColorTokens.TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                content()
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun RtkSectionHeader(
    title: String,
    trailingValue: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = RtkColorTokens.TextMuted
            )
        )
        if (trailingValue != null) {
            Text(
                text = trailingValue,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = RtkColorTokens.AccentCyan
                )
            )
        }
    }
}

enum class ChipAccent {
    CYAN,
    RED
}

/**
 * Pill selectable chip with ergonomic 34dp default height and optional monochrome vector icon.
 */
@Composable
fun RtkSelectableChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: ChipAccent = ChipAccent.CYAN,
    cornerRadius: Dp = 17.dp,
    height: Dp = 34.dp,
    showActiveDot: Boolean = true,
    leadingIcon: ImageVector? = null
) {
    val bgColor = when {
        !selected -> RtkColorTokens.SurfaceChipIdle
        accent == ChipAccent.RED -> RtkColorTokens.SurfaceLimitActiveRed
        else -> RtkColorTokens.SurfaceChipActiveCyan
    }
    val borderColor = when {
        !selected -> RtkColorTokens.BorderMuted
        accent == ChipAccent.RED -> RtkColorTokens.AccentRed
        else -> RtkColorTokens.AccentCyan
    }
    val textColor = when {
        !selected -> RtkColorTokens.TextMuted
        accent == ChipAccent.RED -> RtkColorTokens.AccentRedSoft
        else -> RtkColorTokens.AccentCyan
    }
    val borderWidth = if (selected) 1.5.dp else 1.dp

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(cornerRadius),
        border = BorderStroke(borderWidth, borderColor),
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (selected && showActiveDot) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(textColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = label,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = textColor
                )
            )
        }
    }
}

/**
 * Unified 6dp cyan parameter slider with 18dp white thumb + 2dp cyan ring across all sheets.
 */
@Composable
fun RtkParameterSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    thumbFillColor: Color = RtkColorTokens.TextPrimary,
    thumbBorderColor: Color = RtkColorTokens.AccentCyan
) {
    val clamped = value.coerceIn(0f, 1f)
    var trackWidthPx by remember { mutableFloatStateOf(1f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .onSizeChanged { trackWidthPx = it.width.toFloat().coerceAtLeast(1f) }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onValueChange((offset.x / trackWidthPx).coerceIn(0f, 1f))
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    onValueChange((change.position.x / trackWidthPx).coerceIn(0f, 1f))
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val trackHeight = 6.dp.toPx()
            val thumbRadius = 9.dp.toPx()
            val strokeWidth = 2.dp.toPx()
            val centerY = size.height / 2f
            val usableWidth = (size.width - thumbRadius * 2).coerceAtLeast(1f)
            val thumbCenterX = thumbRadius + clamped * usableWidth

            drawRoundRect(
                color = RtkColorTokens.SurfaceChipIdle,
                topLeft = Offset(0f, centerY - trackHeight / 2f),
                size = Size(size.width, trackHeight),
                cornerRadius = CornerRadius(trackHeight / 2f, trackHeight / 2f)
            )
            drawRoundRect(
                color = RtkColorTokens.AccentCyan,
                topLeft = Offset(0f, centerY - trackHeight / 2f),
                size = Size(thumbCenterX, trackHeight),
                cornerRadius = CornerRadius(trackHeight / 2f, trackHeight / 2f)
            )
            drawCircle(
                color = thumbFillColor,
                radius = thumbRadius,
                center = Offset(thumbCenterX, centerY)
            )
            drawCircle(
                color = thumbBorderColor,
                radius = thumbRadius,
                center = Offset(thumbCenterX, centerY),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}

/**
 * Shared dark toggle card used in AlgorithmTuningSheet and SettingsSheet.
 */
@Composable
fun RtkToggleRowCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = RtkColorTokens.SurfaceToggleCard,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onToggle)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RtkColorTokens.TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = RtkColorTokens.TextMuted
                    )
                )
            }

            val trackColor = if (checked) RtkColorTokens.AccentCyan else RtkColorTokens.SurfaceChipIdle
            val thumbColor = if (checked) RtkColorTokens.CanvasBlack else RtkColorTokens.TextMuted
            val alignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart

            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(trackColor)
                    .padding(3.dp),
                contentAlignment = alignment
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(thumbColor)
                )
            }
        }
    }
}

@Composable
fun RtkInfoBanner(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = RtkColorTokens.SurfaceInfoBox.copy(alpha = 0.9f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, RtkColorTokens.SurfaceChipIdle),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = RtkColorTokens.TextSecondary,
                modifier = Modifier
                    .padding(top = 1.dp)
                    .size(14.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    color = RtkColorTokens.TextMuted
                )
            )
        }
    }
}

/**
 * Two-button modal footer: Secondary dark pill (Reset) + Primary cyan CTA pill.
 */
@Composable
fun RtkSheetFooterButtons(
    secondaryLabel: String,
    primaryLabel: String,
    onSecondaryClick: () -> Unit,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryWeight: Float = 0.42f,
    primaryWeight: Float = 0.58f
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = RtkColorTokens.SurfaceButtonSecondary,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.2.dp, RtkColorTokens.BorderMuted),
            modifier = Modifier
                .weight(secondaryWeight)
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickable(onClick = onSecondaryClick)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = secondaryLabel,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RtkColorTokens.TextMuted
                    )
                )
            }
        }

        Surface(
            color = RtkColorTokens.AccentCyan,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .weight(primaryWeight)
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickable(onClick = onPrimaryClick)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = primaryLabel,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RtkColorTokens.TextOnAccent
                    )
                )
            }
        }
    }
}
