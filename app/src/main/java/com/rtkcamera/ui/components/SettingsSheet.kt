package com.rtkcamera.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtkcamera.ui.AppSettingsState
import com.rtkcamera.ui.theme.RtkColorTokens
import com.rtkcamera.ui.theme.RtkTelemetryTextStyle

/**
 * Hi-Fi 07 • Field & RTK Settings Sheet (accessible from top-right Settings gear).
 */
@Composable
fun SettingsSheet(
    state: AppSettingsState,
    onUpdateState: ((AppSettingsState) -> AppSettingsState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    RtkBottomSheetContainer(
        title = "Field & RTK Settings",
        subtitle = "Viewfinder overlays, telemetry HUD & diagnostic states",
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        // RTK GNSS Telemetry Status Banner
        Surface(
            color = RtkColorTokens.SurfaceToggleCard,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.2.dp, RtkColorTokens.AccentMint.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
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

        Spacer(modifier = Modifier.height(14.dp))
        RtkSectionHeader(title = "COMPOSITION & HUD OVERLAYS")
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
        RtkSectionHeader(title = "UI STATE PREVIEW TOGGLES")
        Spacer(modifier = Modifier.height(8.dp))

        RtkToggleRowCard(
            title = "AI Tracking Bounding Box",
            subtitle = "Preview Hi-Fi 01b object tracking overlay on viewfinder",
            checked = state.showDemoTrackingOverlay,
            onToggle = { onUpdateState { it.copy(showDemoTrackingOverlay = !it.showDemoTrackingOverlay) } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        RtkToggleRowCard(
            title = "Simulate Low-FPS Warning",
            subtitle = "Preview Hi-Fi 03 latency budget exceeded warning card",
            checked = state.simulatePerformanceWarning,
            onToggle = { onUpdateState { it.copy(simulatePerformanceWarning = !it.simulatePerformanceWarning) } }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}
