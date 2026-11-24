package com.rtkcamera.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtkcamera.ui.CaptureMode
import com.rtkcamera.ui.CaptureState
import com.rtkcamera.ui.icons.RtkIcons
import com.rtkcamera.ui.theme.RtkColorTokens

/**
 * Ergonomic Bottom Control Deck matching Hi-Fi 01a, 01b, 01c, 01d, and 03.
 */
@Composable
fun BottomControlDeck(
    captureMode: CaptureMode,
    captureState: CaptureState,
    algorithmPillLabel: String,
    hasProOverrides: Boolean,
    isAutomationArmed: Boolean,
    onShutterClick: () -> Unit,
    onPauseRecordingClick: () -> Unit,
    onProParametersClick: () -> Unit,
    onAutomatedCaptureClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onAlgorithmPillClick: () -> Unit,
    onSelectCaptureMode: (CaptureMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(RtkColorTokens.CanvasBlack)
            .navigationBarsPadding()
            .padding(top = 18.dp, bottom = 22.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (captureState) {
            is CaptureState.Idle -> {
                // Row 1: Pro Parameters | 76dp Tactile Shutter | Automated Capture Timer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DeckCircleActionButton(
                        icon = RtkIcons.Tune,
                        contentDescription = "Pro Camera Parameters",
                        iconTint = RtkColorTokens.AccentCyan,
                        borderColor = if (hasProOverrides) RtkColorTokens.AccentCyan else RtkColorTokens.BorderAction,
                        showActiveBadgeDot = hasProOverrides,
                        badgeDotColor = RtkColorTokens.AccentCyan,
                        onClick = onProParametersClick
                    )

                    TactileShutterButton(
                        captureMode = captureMode,
                        captureState = captureState,
                        onClick = onShutterClick
                    )

                    DeckCircleActionButton(
                        icon = RtkIcons.Timer,
                        contentDescription = "Automated Capture & Intervalometer",
                        iconTint = if (isAutomationArmed) RtkColorTokens.AccentMint else RtkColorTokens.TextPrimary,
                        borderColor = if (isAutomationArmed) RtkColorTokens.AccentMint else RtkColorTokens.BorderAction,
                        showActiveBadgeDot = isAutomationArmed,
                        badgeDotColor = RtkColorTokens.AccentMint,
                        onClick = onAutomatedCaptureClick
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Row 2: Gallery | Algorithm Selector Pill | Dual-State Mode Switch (Photo | Video)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GalleryThumbButton(onClick = onGalleryClick)

                    AlgorithmSelectorPill(
                        label = algorithmPillLabel,
                        onClick = onAlgorithmPillClick,
                        modifier = Modifier.weight(1f)
                    )

                    SegmentedModeSwitchPill(
                        captureMode = captureMode,
                        onSelectCaptureMode = onSelectCaptureMode
                    )
                }
            }

            is CaptureState.VideoRecording -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DeckCircleActionButton(
                        icon = if (captureState.isPaused) RtkIcons.PlayArrow else RtkIcons.Pause,
                        contentDescription = if (captureState.isPaused) "Resume Recording" else "Pause Recording",
                        iconTint = RtkColorTokens.TextPrimary,
                        containerColor = RtkColorTokens.SurfacePauseButton,
                        borderColor = RtkColorTokens.BorderMuted,
                        onClick = onPauseRecordingClick
                    )

                    TactileShutterButton(
                        captureMode = captureMode,
                        captureState = captureState,
                        onClick = onShutterClick
                    )

                    Spacer(modifier = Modifier.size(48.dp))
                }
                Spacer(modifier = Modifier.height(56.dp))
            }

            is CaptureState.PhotoCapturing, is CaptureState.Countdown -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TactileShutterButton(
                        captureMode = captureMode,
                        captureState = captureState,
                        onClick = {}
                    )
                }
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

/**
 * 76dp Tactile Shutter Button with animated inner shape/color across all capture states:
 * - Video Idle: 38dp red circle (#FF1744)
 * - Video Recording: 28dp red rounded square (r=6dp)
 * - Photo Idle: 58dp solid white disc (#FFFFFF)
 * - Photo Capturing / Processing: 48dp cyan core (#00E5FF)
 */
@Composable
fun TactileShutterButton(
    captureMode: CaptureMode,
    captureState: CaptureState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val targetCoreSize = when {
        captureState is CaptureState.PhotoCapturing -> 48.dp
        captureState is CaptureState.VideoRecording -> 28.dp
        captureMode == CaptureMode.VIDEO -> 38.dp
        else -> 58.dp
    }
    val targetCornerRadius = when (captureState) {
        is CaptureState.VideoRecording -> 6.dp
        else -> 29.dp
    }
    val targetCoreColor = when {
        captureState is CaptureState.PhotoCapturing -> RtkColorTokens.AccentCyan
        captureMode == CaptureMode.VIDEO || captureState is CaptureState.VideoRecording -> RtkColorTokens.AccentRed
        else -> RtkColorTokens.TextPrimary
    }

    val coreSize by animateDpAsState(targetValue = targetCoreSize, animationSpec = tween(180), label = "shutterSize")
    val cornerRadius by animateDpAsState(targetValue = targetCornerRadius, animationSpec = tween(180), label = "shutterRadius")
    val coreColor by animateColorAsState(targetValue = targetCoreColor, animationSpec = tween(180), label = "shutterColor")

    Surface(
        color = RtkColorTokens.CanvasBlack.copy(alpha = 0.5f),
        shape = CircleShape,
        border = BorderStroke(3.5.dp, RtkColorTokens.TextPrimary),
        modifier = modifier
            .size(76.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(coreSize)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(coreColor)
            )
        }
    }
}

@Composable
private fun DeckCircleActionButton(
    icon: ImageVector,
    contentDescription: String,
    iconTint: Color,
    borderColor: Color,
    containerColor: Color = RtkColorTokens.SurfaceActionButton,
    showActiveBadgeDot: Boolean = false,
    badgeDotColor: Color = RtkColorTokens.AccentCyan,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.size(48.dp)) {
        Surface(
            color = containerColor.copy(alpha = 0.95f),
            shape = CircleShape,
            border = BorderStroke(1.5.dp, borderColor),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        if (showActiveBadgeDot) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(badgeDotColor)
            )
        }
    }
}

@Composable
private fun GalleryThumbButton(onClick: () -> Unit) {
    Surface(
        color = RtkColorTokens.SurfaceDeckButton.copy(alpha = 0.95f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, RtkColorTokens.BorderMuted),
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = RtkIcons.Gallery,
                contentDescription = "Open Snapshot Inspector",
                tint = RtkColorTokens.TextMuted,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun AlgorithmSelectorPill(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = RtkColorTokens.SurfaceAlgoPill.copy(alpha = 0.95f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.5.dp, RtkColorTokens.AccentCyan),
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = RtkIcons.Flash,
                contentDescription = null,
                tint = RtkColorTokens.AccentAmber,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RtkColorTokens.AccentCyan
                ),
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = RtkIcons.ArrowDropDown,
                contentDescription = "Open Algorithm Tuning",
                tint = RtkColorTokens.AccentCyan,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/**
 * Unambiguous dual-state Photo / Video segmented pill switch.
 */
@Composable
private fun SegmentedModeSwitchPill(
    captureMode: CaptureMode,
    onSelectCaptureMode: (CaptureMode) -> Unit
) {
    val isPhoto = captureMode == CaptureMode.PHOTO
    val isVideo = captureMode == CaptureMode.VIDEO

    Surface(
        color = RtkColorTokens.SurfaceDeckButton.copy(alpha = 0.95f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.5.dp, RtkColorTokens.BorderMuted),
        modifier = Modifier.height(48.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Photo segment
            Surface(
                color = if (isPhoto) RtkColorTokens.AccentCyan.copy(alpha = 0.2f) else Color.Transparent,
                shape = CircleShape,
                border = if (isPhoto) BorderStroke(1.2.dp, RtkColorTokens.AccentCyan) else null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onSelectCaptureMode(CaptureMode.PHOTO) }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = RtkIcons.PhotoCamera,
                        contentDescription = "Photo Mode",
                        tint = if (isPhoto) RtkColorTokens.AccentCyan else RtkColorTokens.TextMuted,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            // Video segment
            Surface(
                color = if (isVideo) RtkColorTokens.AccentRed.copy(alpha = 0.22f) else Color.Transparent,
                shape = CircleShape,
                border = if (isVideo) BorderStroke(1.2.dp, RtkColorTokens.AccentRed) else null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onSelectCaptureMode(CaptureMode.VIDEO) }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = RtkIcons.Videocam,
                        contentDescription = "Video Mode",
                        tint = if (isVideo) RtkColorTokens.AccentRedSoft else RtkColorTokens.TextMuted,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}
