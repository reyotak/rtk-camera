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
 * Symmetrical 3-row Ergonomic Bottom Control Deck:
 * - Row 1: Pro Parameters (48dp) | Tactile Shutter (76dp) | Automated Capture (48dp)
 * - Row 2: Gallery (48dp) | Filter Pill (dead-center) | Help (48dp)
 * - Row 3: Dual-State Photo | Video Mode Strip (dead-center)
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
    onHelpClick: () -> Unit,
    onSelectCaptureMode: (CaptureMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(RtkColorTokens.CanvasBlack)
            .navigationBarsPadding()
            .padding(top = 14.dp, bottom = 14.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (captureState) {
            is CaptureState.Idle -> {
                // Row 1: Pro Parameters (48dp) | 76dp Tactile Shutter | Automated Capture Timer (48dp)
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

                Spacer(modifier = Modifier.height(16.dp))

                // Row 2: Gallery (48dp) | Algorithm Selector Pill (dead-center) | Help (48dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    GalleryThumbButton(onClick = onGalleryClick)

                    AlgorithmSelectorPill(
                        label = algorithmPillLabel,
                        onClick = onAlgorithmPillClick,
                        modifier = Modifier.weight(1f)
                    )

                    HelpDeckButton(onClick = onHelpClick)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Row 3: Centered Photo | Video Segmented Mode Strip
                SegmentedModeSwitchPill(
                    captureMode = captureMode,
                    onSelectCaptureMode = onSelectCaptureMode
                )
            }

            is CaptureState.VideoRecording -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
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
                // Preserves exact shutter vertical alignment with Idle state (zero layout jump)
                Spacer(modifier = Modifier.height(116.dp))
            }

            is CaptureState.PhotoCapturing, is CaptureState.Countdown -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TactileShutterButton(
                        captureMode = captureMode,
                        captureState = captureState,
                        onClick = {}
                    )
                }
                Spacer(modifier = Modifier.height(116.dp))
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
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
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

@Composable
private fun HelpDeckButton(onClick: () -> Unit) {
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
                imageVector = RtkIcons.Help,
                contentDescription = "Help",
                tint = RtkColorTokens.TextMuted,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/**
 * Centered dual-state Photo | Video segmented mode strip.
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
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderMuted),
        modifier = Modifier.height(40.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Photo segment
            Surface(
                color = if (isPhoto) RtkColorTokens.AccentCyan.copy(alpha = 0.2f) else Color.Transparent,
                shape = RoundedCornerShape(17.dp),
                border = if (isPhoto) BorderStroke(1.2.dp, RtkColorTokens.AccentCyan) else null,
                modifier = Modifier
                    .height(34.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .clickable { onSelectCaptureMode(CaptureMode.PHOTO) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = RtkIcons.PhotoCamera,
                        contentDescription = "Photo Mode",
                        tint = if (isPhoto) RtkColorTokens.AccentCyan else RtkColorTokens.TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "PHOTO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isPhoto) FontWeight.Bold else FontWeight.Medium,
                            color = if (isPhoto) RtkColorTokens.AccentCyan else RtkColorTokens.TextMuted
                        )
                    )
                }
            }

            // Video segment
            Surface(
                color = if (isVideo) RtkColorTokens.AccentRed.copy(alpha = 0.22f) else Color.Transparent,
                shape = RoundedCornerShape(17.dp),
                border = if (isVideo) BorderStroke(1.2.dp, RtkColorTokens.AccentRed) else null,
                modifier = Modifier
                    .height(34.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .clickable { onSelectCaptureMode(CaptureMode.VIDEO) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = RtkIcons.Videocam,
                        contentDescription = "Video Mode",
                        tint = if (isVideo) RtkColorTokens.AccentRedSoft else RtkColorTokens.TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "VIDEO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isVideo) FontWeight.Bold else FontWeight.Medium,
                            color = if (isVideo) RtkColorTokens.AccentRedSoft else RtkColorTokens.TextMuted
                        )
                    )
                }
            }
        }
    }
}
