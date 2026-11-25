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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtkcamera.ui.AppSettingsState
import com.rtkcamera.ui.HelpSection
import com.rtkcamera.ui.icons.RtkIcons
import com.rtkcamera.ui.theme.RtkColorTokens
import com.rtkcamera.ui.theme.RtkTelemetryTextStyle

/**
 * Help & Info Sheet (replaces Settings button on bottom deck):
 * - Interactive Tutorial
 * - Controls & HUD Color Legend
 * - About RTK-Camera (version, architecture, Apache 2.0 license)
 * - HUD Overlays & RTK Diagnostics toggles
 */
@Composable
fun HelpSheet(
    state: AppSettingsState,
    onUpdateState: ((AppSettingsState) -> AppSettingsState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (title, subtitle) = when (state.activeHelpSection) {
        HelpSection.MENU -> "Help & Field Guide" to "Tutorials, HUD reference, diagnostics & app info"
        HelpSection.TUTORIAL -> "Quick Start Tutorial" to "Get started with RTK-Camera in 4 steps"
        HelpSection.CONTROLS_GUIDE -> "Controls & Color Legend" to "HUD status indicators and tactile controls"
        HelpSection.ABOUT -> "About RTK-Camera" to "Version, native sandbox & open-source license"
    }

    RtkBottomSheetContainer(
        title = title,
        subtitle = subtitle,
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        when (state.activeHelpSection) {
            HelpSection.MENU -> {
                HelpMainMenuContent(
                    state = state,
                    onSelectSection = { section ->
                        onUpdateState { it.copy(activeHelpSection = section) }
                    },
                    onUpdateState = onUpdateState
                )
            }

            HelpSection.TUTORIAL -> {
                BackToHelpMenuRow(
                    onBack = { onUpdateState { it.copy(activeHelpSection = HelpSection.MENU) } }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TutorialStepCard(
                    step = "01",
                    title = "Frame & Tap to Focus",
                    body = "Tap anywhere on the live viewfinder to place the cyan focus reticle. Use .5, 1x, or 2 to switch focal lengths, even while recording video."
                )
                Spacer(modifier = Modifier.height(8.dp))
                TutorialStepCard(
                    step = "02",
                    title = "Tune Real-Time Filters",
                    body = "Tap the center filter pill to switch native C++ plugins (Raw, Sobel 2D, Canny v2.1, Depth), adjust thresholds, and enable RAW DNG + JPEG capture."
                )
                Spacer(modifier = Modifier.height(8.dp))
                TutorialStepCard(
                    step = "03",
                    title = "Pro Optics & Intervalometer",
                    body = "Use the Tune button (left of shutter) for Aspect Ratio, HDR bracketing, Kelvin WB, Manual Focus, Shutter, and ISO. Use Timer (right) to arm burst or countdown sessions."
                )
                Spacer(modifier = Modifier.height(8.dp))
                TutorialStepCard(
                    step = "04",
                    title = "Review High-Res Snapshots",
                    body = "Tap the Gallery button (bottom-left) to open Snapshot Inspector and inspect resolution, compute latency, exposure metadata, and export options."
                )
            }

            HelpSection.CONTROLS_GUIDE -> {
                BackToHelpMenuRow(
                    onBack = { onUpdateState { it.copy(activeHelpSection = HelpSection.MENU) } }
                )
                Spacer(modifier = Modifier.height(12.dp))
                RtkSectionHeader(title = "HUD COLOR LANGUAGE")
                Spacer(modifier = Modifier.height(8.dp))
                ColorLegendRow(
                    color = RtkColorTokens.AccentCyan,
                    title = "Electric Cyan",
                    description = "Active algorithm, Photo mode, locked focus & Pro overrides"
                )
                Spacer(modifier = Modifier.height(6.dp))
                ColorLegendRow(
                    color = RtkColorTokens.AccentMint,
                    title = "RTK Mint",
                    description = "Centimeter RTK Fix lock, armed automation & saved confirmation"
                )
                Spacer(modifier = Modifier.height(6.dp))
                ColorLegendRow(
                    color = RtkColorTokens.AccentAmber,
                    title = "Telemetry Amber",
                    description = "Pipeline latency warning (>33ms / <30 FPS) & Flash Auto"
                )
                Spacer(modifier = Modifier.height(6.dp))
                ColorLegendRow(
                    color = RtkColorTokens.AccentRed,
                    title = "Signal Red",
                    description = "Video mode, active recording timer & video clip limits"
                )

                Spacer(modifier = Modifier.height(14.dp))
                RtkSectionHeader(title = "SHUTTER STATES")
                Spacer(modifier = Modifier.height(8.dp))
                RtkInfoBanner(
                    icon = RtkIcons.Info,
                    text = "White Disc = Photo Ready • Cyan Core = Capturing / Processing • Red Circle = Video Ready • Red Square = Recording (Tap to Stop)"
                )
            }

            HelpSection.ABOUT -> {
                BackToHelpMenuRow(
                    onBack = { onUpdateState { it.copy(activeHelpSection = HelpSection.MENU) } }
                )
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = RtkColorTokens.SurfaceToggleCard,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, RtkColorTokens.BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "RTK-Camera",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RtkColorTokens.TextPrimary
                                )
                            )
                            Surface(
                                color = RtkColorTokens.SurfaceChipActiveCyan,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, RtkColorTokens.AccentCyan)
                            ) {
                                Text(
                                    text = "v1.0.0",
                                    style = RtkTelemetryTextStyle.copy(
                                        fontSize = 10.sp,
                                        color = RtkColorTokens.AccentCyan
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        Text(
                            text = "Real-time field camera with pluggable native C++ computer-vision algorithm sandbox and high-contrast Material 3 telemetry HUD.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = RtkColorTokens.TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AboutMetaRow(label = "ARCHITECTURE", value = "Kotlin • Jetpack Compose • CameraX • C++17 NDK")
                Spacer(modifier = Modifier.height(6.dp))
                AboutMetaRow(label = "FRAME BUDGET TARGET", value = "≤ 33.3ms (30+ FPS Real-Time Pipeline)")
                Spacer(modifier = Modifier.height(6.dp))
                AboutMetaRow(label = "LICENSE", value = "Apache License 2.0 (Open Source)")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun HelpMainMenuContent(
    state: AppSettingsState,
    onSelectSection: (HelpSection) -> Unit,
    onUpdateState: ((AppSettingsState) -> AppSettingsState) -> Unit
) {
    // Primary Help Navigation Rows
    RtkSectionHeader(title = "GUIDE & INFORMATION")
    Spacer(modifier = Modifier.height(8.dp))

    HelpNavigationRow(
        icon = RtkIcons.MenuBook,
        iconTint = RtkColorTokens.AccentCyan,
        title = "Quick Start Tutorial",
        subtitle = "Step-by-step walkthrough of viewfinder, filters & capture modes",
        onClick = { onSelectSection(HelpSection.TUTORIAL) }
    )

    Spacer(modifier = Modifier.height(8.dp))

    HelpNavigationRow(
        icon = RtkIcons.CenterFocus,
        iconTint = RtkColorTokens.AccentMint,
        title = "Controls & HUD Color Legend",
        subtitle = "Learn what each indicator, shutter state & status color means",
        onClick = { onSelectSection(HelpSection.CONTROLS_GUIDE) }
    )

    Spacer(modifier = Modifier.height(8.dp))

    HelpNavigationRow(
        icon = RtkIcons.Info,
        iconTint = RtkColorTokens.TextSecondary,
        title = "About RTK-Camera",
        subtitle = "Version 1.0.0 • Native C++ Sandbox • Apache 2.0 License",
        onClick = { onSelectSection(HelpSection.ABOUT) }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Diagnostics & Viewfinder Overlay Toggles
    RtkSectionHeader(title = "RTK STATUS & VIEWFINDER OVERLAYS")
    Spacer(modifier = Modifier.height(8.dp))

    Surface(
        color = RtkColorTokens.SurfaceToggleCard,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.2.dp, RtkColorTokens.AccentMint.copy(alpha = 0.7f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(RtkColorTokens.AccentMint)
            )
            Text(
                text = state.rtkStatusText,
                style = RtkTelemetryTextStyle.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RtkColorTokens.AccentMint
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    RtkToggleRowCard(
        title = "Rule-of-Thirds Grid",
        subtitle = "Display subtle 3×3 alignment grid over live viewfinder",
        checked = state.showRuleOfThirdsGrid,
        onToggle = { onUpdateState { it.copy(showRuleOfThirdsGrid = !it.showRuleOfThirdsGrid) } }
    )

    Spacer(modifier = Modifier.height(8.dp))

    RtkToggleRowCard(
        title = "Aspect Ratio Framing Guides",
        subtitle = "Mask viewfinder edges to match selected Pro resolution ratio",
        checked = state.showAspectFramingGuides,
        onToggle = { onUpdateState { it.copy(showAspectFramingGuides = !it.showAspectFramingGuides) } }
    )

    Spacer(modifier = Modifier.height(8.dp))

    RtkToggleRowCard(
        title = "Telemetry FPS & Latency Pill",
        subtitle = "Show real-time pipeline frame rate and latency in top-left",
        checked = state.showTelemetryHud,
        onToggle = { onUpdateState { it.copy(showTelemetryHud = !it.showTelemetryHud) } }
    )

    Spacer(modifier = Modifier.height(14.dp))
    RtkSectionHeader(title = "UI STATE PREVIEW DEMOS")
    Spacer(modifier = Modifier.height(8.dp))

    RtkToggleRowCard(
        title = "AI Tracking Bounding Box",
        subtitle = "Preview object tracking overlay on viewfinder",
        checked = state.showDemoTrackingOverlay,
        onToggle = { onUpdateState { it.copy(showDemoTrackingOverlay = !it.showDemoTrackingOverlay) } }
    )

    Spacer(modifier = Modifier.height(8.dp))

    RtkToggleRowCard(
        title = "Simulate Low-FPS Warning",
        subtitle = "Preview >33ms latency budget exceeded warning card",
        checked = state.simulatePerformanceWarning,
        onToggle = { onUpdateState { it.copy(simulatePerformanceWarning = !it.simulatePerformanceWarning) } }
    )
}

@Composable
private fun HelpNavigationRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        color = RtkColorTokens.SurfaceToggleCard,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = RtkColorTokens.SurfaceChipIdle,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 13.sp,
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
            Icon(
                imageVector = RtkIcons.ChevronRight,
                contentDescription = null,
                tint = RtkColorTokens.TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun BackToHelpMenuRow(onBack: () -> Unit) {
    Surface(
        color = RtkColorTokens.SurfaceChipIdle,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderMuted),
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onBack)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = RtkIcons.ArrowBack,
                contentDescription = "Back to Help Menu",
                tint = RtkColorTokens.AccentCyan,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = "Back to Help Menu",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RtkColorTokens.AccentCyan
                )
            )
        }
    }
}

@Composable
private fun TutorialStepCard(
    step: String,
    title: String,
    body: String
) {
    Surface(
        color = RtkColorTokens.SurfaceToggleCard,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = RtkColorTokens.SurfaceChipActiveCyan,
                shape = CircleShape,
                border = BorderStroke(1.dp, RtkColorTokens.AccentCyan),
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = step,
                        style = RtkTelemetryTextStyle.copy(
                            fontSize = 10.sp,
                            color = RtkColorTokens.AccentCyan
                        )
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RtkColorTokens.TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        color = RtkColorTokens.TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
private fun ColorLegendRow(
    color: Color,
    title: String,
    description: String
) {
    Surface(
        color = RtkColorTokens.SurfaceToggleCard,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = RtkColorTokens.TextMuted
                    )
                )
            }
        }
    }
}

@Composable
private fun AboutMetaRow(
    label: String,
    value: String
) {
    Surface(
        color = RtkColorTokens.SurfaceMetaCard,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    color = RtkColorTokens.TextMuted
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RtkColorTokens.TextPrimary
                )
            )
        }
    }
}
