package com.rtkcamera.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtkcamera.ui.AspectResolutionPreset
import com.rtkcamera.ui.BracketFrame
import com.rtkcamera.ui.IsoPreset
import com.rtkcamera.ui.ProParametersState
import com.rtkcamera.ui.ShutterSpeedPreset
import com.rtkcamera.ui.WhiteBalancePreset
import com.rtkcamera.ui.icons.RtkIcons
import com.rtkcamera.ui.theme.RtkColorTokens

/**
 * Hi-Fi 05 • Pro Camera Parameters Sheet.
 * Uses full-width stacked Shutter Speed and ISO rows, monochrome vector WB icons,
 * and unified white-fill / cyan-ring slider styling.
 */
@Composable
fun ProParametersSheet(
    state: ProParametersState,
    onUpdateState: ((ProParametersState) -> ProParametersState) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    RtkBottomSheetContainer(
        title = "Pro Camera Parameters",
        subtitle = "Resolution, WB, Focus, Shutter & ISO (with HDR Bracketing)",
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        // Aspect Ratio & Resolution
        RtkSectionHeader(title = "ASPECT RATIO & RESOLUTION")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AspectResolutionPreset.entries.forEach { preset ->
                RtkSelectableChip(
                    label = preset.label,
                    selected = state.aspectResolution == preset,
                    onClick = { onUpdateState { it.copy(aspectResolution = preset) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Multi-Frame Bracketing
        RtkSectionHeader(title = "MULTI-FRAME BRACKETING (PER-FRAME OVERRIDES)")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BracketFrame.entries.forEach { bracket ->
                RtkSelectableChip(
                    label = bracket.label,
                    selected = state.selectedBracket == bracket,
                    onClick = { onUpdateState { it.copy(selectedBracket = bracket) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // White Balance with monochrome vector icons
        RtkSectionHeader(title = "WHITE BALANCE (KELVIN)")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WhiteBalancePreset.entries.forEach { wb ->
                val icon = when (wb) {
                    WhiteBalancePreset.AWB -> null
                    WhiteBalancePreset.DAYLIGHT -> RtkIcons.WbSunny
                    WhiteBalancePreset.CLOUDY -> RtkIcons.Cloud
                    WhiteBalancePreset.TUNGSTEN -> RtkIcons.Lightbulb
                }
                RtkSelectableChip(
                    label = wb.label,
                    selected = state.whiteBalance == wb,
                    leadingIcon = icon,
                    onClick = { onUpdateState { it.copy(whiteBalance = wb) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Manual Focus & Focus Peaking (unified slider thumb styling)
        RtkSectionHeader(
            title = "MANUAL FOCUS & FOCUS PEAKING",
            trailingValue = state.focusDistanceLabel
        )
        Spacer(modifier = Modifier.height(4.dp))
        RtkParameterSlider(
            value = state.manualFocusProgress,
            onValueChange = { progress ->
                onUpdateState { it.copy(manualFocusProgress = progress) }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Macro (0.1m)",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 9.sp,
                    color = RtkColorTokens.TextMuted
                )
            )
            Text(
                text = "Infinity (∞)",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 9.sp,
                    color = RtkColorTokens.TextMuted
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Full-width Shutter Speed row
        RtkSectionHeader(title = "SHUTTER SPEED (S)")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShutterSpeedPreset.entries.forEach { shutter ->
                RtkSelectableChip(
                    label = shutter.label,
                    selected = state.shutterSpeed == shutter,
                    onClick = { onUpdateState { it.copy(shutterSpeed = shutter) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Full-width ISO Sensitivity row
        RtkSectionHeader(title = "ISO SENSITIVITY")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IsoPreset.entries.forEach { iso ->
                RtkSelectableChip(
                    label = iso.label,
                    selected = state.iso == iso,
                    onClick = { onUpdateState { it.copy(iso = iso) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Per-Frame Rule Info Banner
        RtkInfoBanner(
            icon = RtkIcons.Info,
            text = "Per-Frame Rule: When HDR or Burst algorithms run, Frame 2 (0 EV) uses these parameters while Bracketed Frames 1 & 3 auto-calculate EV offsets."
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Footer Actions
        RtkSheetFooterButtons(
            secondaryLabel = "Reset to AWB / Auto",
            primaryLabel = "Apply Parameters",
            onSecondaryClick = onReset,
            onPrimaryClick = onApply,
            secondaryWeight = 0.45f,
            primaryWeight = 0.55f
        )
    }
}
