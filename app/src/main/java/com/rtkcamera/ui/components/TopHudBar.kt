package com.rtkcamera.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtkcamera.ui.CaptureState
import com.rtkcamera.ui.formatRecordingTimer
import com.rtkcamera.ui.icons.RtkIcons
import com.rtkcamera.ui.theme.RtkColorTokens
import com.rtkcamera.ui.theme.RtkTelemetryTextStyle

/**
 * Floating transparent Top Preview Overlay rendered directly over the edge-to-edge viewfinder:
 * - Idle: Top-left FPS pill + optional active Pro/Armed badges, Top-right Settings button (48dp hitbox)
 * - Countdown: Top-left FPS pill
 * - Video Recording: Top-center REC timer pill (or compact left badge with Performance Warning Card)
 * - Photo Capturing: Top-left FPS pill + optional Burst progress pill
 */
@Composable
fun TopPreviewOverlay(
    captureState: CaptureState,
    fps: Float,
    latencyMs: Float,
    frameDropPercent: Int,
    algorithmName: String,
    showTelemetryHud: Boolean,
    proSummaryBadge: String?,
    armedSummaryBadge: String?,
    isPerformanceWarningVisible: Boolean,
    onSettingsClick: () -> Unit,
    onOptimizeClick: () -> Unit,
    onDismissWarningClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (captureState) {
            is CaptureState.Idle -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (showTelemetryHud && !isPerformanceWarningVisible) {
                        TelemetryFpsPill(
                            fps = fps,
                            latencyMs = latencyMs,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                    SettingsPillButton(onClick = onSettingsClick)
                }

                if (isPerformanceWarningVisible) {
                    PerformanceWarningCard(
                        fps = fps,
                        latencyMs = latencyMs,
                        frameDropPercent = frameDropPercent,
                        algorithmName = algorithmName,
                        onOptimizeClick = onOptimizeClick,
                        onDismissClick = onDismissWarningClick,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                } else if (proSummaryBadge != null || armedSummaryBadge != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (proSummaryBadge != null) {
                            ActiveStatusBadge(
                                text = proSummaryBadge,
                                accentColor = RtkColorTokens.AccentCyan
                            )
                        }
                        if (armedSummaryBadge != null) {
                            ActiveStatusBadge(
                                text = armedSummaryBadge,
                                accentColor = RtkColorTokens.AccentMint
                            )
                        }
                    }
                }
            }

            is CaptureState.Countdown -> {
                if (showTelemetryHud) {
                    TelemetryFpsPill(
                        fps = fps,
                        latencyMs = latencyMs,
                        modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                    )
                }
            }

            is CaptureState.VideoRecording -> {
                val formatted = formatRecordingTimer(captureState.elapsedSeconds)
                val recText = if (captureState.isPaused) "PAUSED  $formatted" else "REC  $formatted"

                if (isPerformanceWarningVisible) {
                    Surface(
                        color = RtkColorTokens.SurfacePillDark.copy(alpha = 0.78f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RecordingDot()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = recText,
                                style = RtkTelemetryTextStyle.copy(
                                    fontSize = 12.sp,
                                    color = RtkColorTokens.TextPrimary
                                )
                            )
                        }
                    }

                    PerformanceWarningCard(
                        fps = fps,
                        latencyMs = latencyMs,
                        frameDropPercent = frameDropPercent,
                        algorithmName = algorithmName,
                        onOptimizeClick = onOptimizeClick,
                        onDismissClick = onDismissWarningClick,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = RtkColorTokens.SurfaceRecordingPill.copy(alpha = 0.95f),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.5.dp, RtkColorTokens.AccentRed),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                RecordingDot()
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = recText,
                                    style = RtkTelemetryTextStyle.copy(
                                        fontSize = 12.sp,
                                        color = RtkColorTokens.TextPrimary
                                    )
                                )
                            }
                        }
                    }

                    if (showTelemetryHud) {
                        TelemetryFpsPill(
                            fps = fps,
                            latencyMs = latencyMs,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            is CaptureState.PhotoCapturing -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp, top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showTelemetryHud) {
                        TelemetryFpsPill(fps = fps, latencyMs = latencyMs)
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    if (captureState.burstTotal > 1) {
                        ActiveStatusBadge(
                            text = "CAPTURING ${captureState.burstCurrent} / ${captureState.burstTotal}",
                            accentColor = RtkColorTokens.AccentCyan
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveStatusBadge(
    text: String,
    accentColor: Color
) {
    Surface(
        color = RtkColorTokens.SurfacePillDark.copy(alpha = 0.85f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.65f)),
        modifier = Modifier.height(24.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            )
        }
    }
}

/**
 * Settings gear button with full 48x48dp ergonomic touch target.
 */
@Composable
private fun SettingsPillButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = RtkColorTokens.SurfaceControl.copy(alpha = 0.85f),
            shape = CircleShape,
            border = BorderStroke(1.dp, RtkColorTokens.BorderControl),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = RtkIcons.Settings,
                    contentDescription = "Settings",
                    tint = RtkColorTokens.TextPrimary,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}

@Composable
private fun RecordingDot() {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(Color(0xFFFF2D2D))
    )
}
