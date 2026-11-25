package com.rtkcamera.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtkcamera.ui.AspectResolutionPreset
import com.rtkcamera.ui.FlashMode
import com.rtkcamera.ui.FocusReticleState
import com.rtkcamera.ui.TrackingOverlayState
import com.rtkcamera.ui.ZoomLevel
import com.rtkcamera.ui.icons.RtkIcons
import com.rtkcamera.ui.theme.RtkColorTokens
import com.rtkcamera.ui.theme.RtkTelemetryTextStyle
import kotlin.math.roundToInt

/**
 * Soft top scrim ensuring status bar & floating HUD pills stay legible in bright sunlight.
 */
@Composable
fun TopViewfinderScrim(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.55f),
                        Color.Black.copy(alpha = 0.18f),
                        Color.Transparent
                    )
                )
            )
    )
}

/**
 * Soft bottom-of-viewfinder scrim behind Camera Switch, Zoom Bar, and Flash controls.
 */
@Composable
fun BottomViewfinderScrim(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.35f),
                        Color.Black.copy(alpha = 0.65f)
                    )
                )
            )
    )
}

/**
 * Subtle Rule-of-Thirds composition grid toggled via Settings.
 */
@Composable
fun RuleOfThirdsGridOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val lineColor = Color.White.copy(alpha = 0.22f)
        val stroke = 1.dp.toPx()
        val thirdW = size.width / 3f
        val thirdH = size.height / 3f
        for (i in 1..2) {
            drawLine(
                color = lineColor,
                start = Offset(thirdW * i, 0f),
                end = Offset(thirdW * i, size.height),
                strokeWidth = stroke
            )
            drawLine(
                color = lineColor,
                start = Offset(0f, thirdH * i),
                end = Offset(size.width, thirdH * i),
                strokeWidth = stroke
            )
        }
    }
}

/**
 * Translucent crop/framing guide matching the selected Pro Aspect Ratio preset.
 */
@Composable
fun AspectRatioFramingOverlay(
    preset: AspectResolutionPreset,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val targetRatio = preset.ratioValue // width / height in portrait
        val currentRatio = size.width / size.height
        val maskColor = Color.Black.copy(alpha = 0.48f)
        val guideBorder = RtkColorTokens.AccentCyan.copy(alpha = 0.45f)
        val stroke = 1.dp.toPx()

        if (targetRatio > currentRatio) {
            // Letterbox top & bottom (e.g., 1:1 or 4:3 inside tall portrait viewfinder)
            val cropHeight = size.width / targetRatio
            val barHeight = ((size.height - cropHeight) / 2f).coerceAtLeast(0f)
            if (barHeight > 2f) {
                drawRect(color = maskColor, topLeft = Offset.Zero, size = androidx.compose.ui.geometry.Size(size.width, barHeight))
                drawRect(color = maskColor, topLeft = Offset(0f, size.height - barHeight), size = androidx.compose.ui.geometry.Size(size.width, barHeight))
                drawLine(color = guideBorder, start = Offset(0f, barHeight), end = Offset(size.width, barHeight), strokeWidth = stroke)
                drawLine(color = guideBorder, start = Offset(0f, size.height - barHeight), end = Offset(size.width, size.height - barHeight), strokeWidth = stroke)
            }
        }
    }
}

/**
 * Cyan corner-bracket focus reticle rendered where the user taps the viewfinder.
 */
@Composable
fun FocusReticleOverlay(
    focusReticle: FocusReticleState,
    modifier: Modifier = Modifier
) {
    if (!focusReticle.visible) return

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val reticleSize = 68.dp
        val xPx = (constraints.maxWidth * focusReticle.normalizedX).roundToInt()
        val yPx = (constraints.maxHeight * focusReticle.normalizedY).roundToInt()

        Canvas(
            modifier = Modifier
                .offset {
                    val half = (reticleSize.toPx() / 2f).roundToInt()
                    IntOffset(xPx - half, yPx - half)
                }
                .size(reticleSize)
        ) {
            val bracketLen = 14.dp.toPx()
            val strokePx = 2.dp.toPx()
            val color = RtkColorTokens.AccentCyan

            // Top-left bracket
            drawLine(color, Offset(0f, 0f), Offset(bracketLen, 0f), strokePx, StrokeCap.Round)
            drawLine(color, Offset(0f, 0f), Offset(0f, bracketLen), strokePx, StrokeCap.Round)
            // Top-right bracket
            drawLine(color, Offset(size.width - bracketLen, 0f), Offset(size.width, 0f), strokePx, StrokeCap.Round)
            drawLine(color, Offset(size.width, 0f), Offset(size.width, bracketLen), strokePx, StrokeCap.Round)
            // Bottom-left bracket
            drawLine(color, Offset(0f, size.height), Offset(bracketLen, size.height), strokePx, StrokeCap.Round)
            drawLine(color, Offset(0f, size.height - bracketLen), Offset(0f, size.height), strokePx, StrokeCap.Round)
            // Bottom-right bracket
            drawLine(color, Offset(size.width - bracketLen, size.height), Offset(size.width, size.height), strokePx, StrokeCap.Round)
            drawLine(color, Offset(size.width, size.height - bracketLen), Offset(size.width, size.height), strokePx, StrokeCap.Round)
            // Center crosshair dot
            drawCircle(color = color, radius = 2.5.dp.toPx(), center = center)
        }
    }
}

/**
 * Centered translucent shutter countdown overlay (3… 2… 1…).
 */
@Composable
fun CountdownOverlay(
    secondsRemaining: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = RtkColorTokens.SurfacePillDark.copy(alpha = 0.72f),
            shape = CircleShape,
            border = BorderStroke(2.dp, RtkColorTokens.AccentCyan),
            modifier = Modifier.size(112.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = secondsRemaining.toString(),
                    style = RtkTelemetryTextStyle.copy(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = RtkColorTokens.AccentCyan
                    )
                )
            }
        }
    }
}

/**
 * Floating top-left telemetry pill (Hi-Fi 01a/01b/01c/01d).
 */
@Composable
fun TelemetryFpsPill(
    fps: Float,
    latencyMs: Float,
    modifier: Modifier = Modifier
) {
    Surface(
        color = RtkColorTokens.SurfacePillDark.copy(alpha = 0.85f),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderAction),
        modifier = modifier.height(28.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(RtkColorTokens.AccentCyan)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = String.format("%.1f FPS • %.1fms", fps, latencyMs),
                style = RtkTelemetryTextStyle.copy(
                    fontSize = 10.sp,
                    color = RtkColorTokens.AccentCyan
                )
            )
        }
    }
}

/**
 * Extended Performance Warning Card (Hi-Fi 03 • Performance Warning State).
 */
@Composable
fun PerformanceWarningCard(
    fps: Float,
    latencyMs: Float,
    frameDropPercent: Int,
    algorithmName: String,
    onOptimizeClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = RtkColorTokens.SurfaceWarningCard.copy(alpha = 0.95f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, RtkColorTokens.AccentAmber),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = RtkIcons.Warning,
                    contentDescription = "Performance Warning",
                    tint = RtkColorTokens.AccentAmber,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = String.format("%.0f FPS • %.1fms (Budget: 33ms Exceeded)", fps, latencyMs),
                    style = RtkTelemetryTextStyle.copy(
                        fontSize = 11.sp,
                        color = RtkColorTokens.AccentAmber
                    )
                )
            }

            Text(
                text = String.format(
                    "C++ pipeline latency: %.1fms. Frame drops: %d%%.\nHeavy %s algorithm exceeds real-time streaming budget.",
                    latencyMs,
                    frameDropPercent,
                    algorithmName
                ),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    color = RtkColorTokens.TextSecondary
                )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Surface(
                    color = RtkColorTokens.AccentAmber,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onOptimizeClick)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Optimize Parameters",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = RtkColorTokens.TextOnAccent
                            )
                        )
                    }
                }

                Surface(
                    color = RtkColorTokens.SurfaceChipIdle,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, RtkColorTokens.BorderMuted),
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onDismissClick)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Dismiss",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RtkColorTokens.TextMuted
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * AI Bounding Box + Feedback Pill Overlay (Hi-Fi 01b) with monochrome vector icon.
 */
@Composable
fun AiTrackingOverlay(
    trackingState: TrackingOverlayState,
    modifier: Modifier = Modifier
) {
    if (!trackingState.visible) return

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Surface(
            color = RtkColorTokens.AccentCyan,
            shape = RoundedCornerShape(13.dp),
            modifier = Modifier.height(26.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    imageVector = RtkIcons.CenterFocus,
                    contentDescription = null,
                    tint = RtkColorTokens.TextOnAccent,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = trackingState.label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RtkColorTokens.TextOnAccent
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            color = RtkColorTokens.AccentCyan.copy(alpha = 0.05f),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.5.dp, RtkColorTokens.AccentCyan),
            modifier = Modifier.size(220.dp)
        ) {}
    }
}

/**
 * Optical shutter exposure flash scrim (Hi-Fi 01d).
 */
@Composable
fun ShutterFlashOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.35f))
    )
}

/**
 * Bottom-of-viewfinder overlay bar containing:
 * - Camera Switch Button (bottom-left)
 * - Circular Zoom Bar (bottom-center): .5 / 1x / 2 (with 48dp hit targets)
 * - Flash Toggle Button (bottom-right) with distinct Off/On/Auto icons
 */
@Composable
fun ViewfinderBottomOverlayBar(
    selectedZoom: ZoomLevel,
    flashMode: FlashMode,
    showSideControls: Boolean,
    onCameraSwitchClick: () -> Unit,
    onSelectZoom: (ZoomLevel) -> Unit,
    onCycleFlashMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        if (showSideControls) {
            CameraSwitchOverlayButton(
                onClick = onCameraSwitchClick,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        CircularZoomBar(
            selectedZoom = selectedZoom,
            onSelectZoom = onSelectZoom,
            modifier = Modifier.align(Alignment.Center)
        )
        if (showSideControls) {
            FlashOverlayButton(
                flashMode = flashMode,
                onClick = onCycleFlashMode,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun CameraSwitchOverlayButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = RtkColorTokens.SurfaceModeToggle.copy(alpha = 0.85f),
        shape = CircleShape,
        border = BorderStroke(1.5.dp, RtkColorTokens.BorderMuted),
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = RtkIcons.CameraSwitch,
                contentDescription = "Switch Camera",
                tint = RtkColorTokens.TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun FlashOverlayButton(
    flashMode: FlashMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isActive = flashMode != FlashMode.OFF
    val borderColor = when (flashMode) {
        FlashMode.ON -> RtkColorTokens.AccentCyan
        FlashMode.AUTO -> RtkColorTokens.AccentAmber
        FlashMode.OFF -> RtkColorTokens.BorderControl
    }
    val borderWidth = if (isActive) 1.5.dp else 1.dp
    val iconTint = when (flashMode) {
        FlashMode.ON -> RtkColorTokens.AccentCyan
        FlashMode.AUTO -> RtkColorTokens.AccentAmber
        FlashMode.OFF -> RtkColorTokens.TextMuted
    }
    val icon = when (flashMode) {
        FlashMode.OFF -> RtkIcons.FlashOff
        FlashMode.ON -> RtkIcons.Flash
        FlashMode.AUTO -> RtkIcons.FlashAuto
    }

    Surface(
        color = RtkColorTokens.SurfaceModeToggle.copy(alpha = 0.85f),
        shape = CircleShape,
        border = BorderStroke(borderWidth, borderColor),
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = flashMode.label,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/**
 * Pixel-style unified Zoom Pill (.5 / 1x / 2) nested inside a single frosted dark capsule.
 */
@Composable
fun CircularZoomBar(
    selectedZoom: ZoomLevel,
    onSelectZoom: (ZoomLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = RtkColorTokens.SurfaceModeToggle.copy(alpha = 0.86f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderMuted),
        modifier = modifier.height(44.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ZoomLevel.entries.forEach { zoom ->
                val isSelected = zoom == selectedZoom
                val bg = if (isSelected) {
                    RtkColorTokens.AccentCyan.copy(alpha = 0.95f)
                } else {
                    Color.Transparent
                }
                val textColor = if (isSelected) {
                    RtkColorTokens.TextOnAccent
                } else {
                    RtkColorTokens.TextZoomIdle
                }

                Surface(
                    color = bg,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onSelectZoom(zoom) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = zoom.label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = if (isSelected) 12.sp else 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = textColor
                            )
                        )
                    }
                }
            }
        }
    }
}
