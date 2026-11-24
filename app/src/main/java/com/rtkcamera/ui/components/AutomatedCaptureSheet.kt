package com.rtkcamera.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rtkcamera.ui.AutomatedCaptureState
import com.rtkcamera.ui.BurstCountOption
import com.rtkcamera.ui.CountdownTimerOption
import com.rtkcamera.ui.IntervalDelayOption
import com.rtkcamera.ui.MaxVideoDurationOption
import com.rtkcamera.ui.icons.RtkIcons

/**
 * Hi-Fi 06 • Automated Capture & Intervalometer Sheet.
 */
@Composable
fun AutomatedCaptureSheet(
    state: AutomatedCaptureState,
    onUpdateState: ((AutomatedCaptureState) -> AutomatedCaptureState) -> Unit,
    onReset: () -> Unit,
    onArm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    RtkBottomSheetContainer(
        title = "Automated Capture & Intervalometer",
        subtitle = "Configure burst capture, intervals & video record limits",
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        // Burst Capture Count
        RtkSectionHeader(
            title = "BURST CAPTURE COUNT (FRAMES)",
            trailingValue = state.burstSummaryLabel
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BurstCountOption.entries.forEach { option ->
                RtkSelectableChip(
                    label = option.chipLabel,
                    selected = state.burstCount == option,
                    onClick = { onUpdateState { it.copy(burstCount = option) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Time Between Captures (Interval Delay)
        RtkSectionHeader(
            title = "TIME BETWEEN CAPTURES (INTERVAL DELAY)",
            trailingValue = state.intervalDelay.descriptor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IntervalDelayOption.entries.forEach { option ->
                RtkSelectableChip(
                    label = option.chipLabel,
                    selected = state.intervalDelay == option,
                    onClick = { onUpdateState { it.copy(intervalDelay = option) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Max Recording Duration Limit (Video Mode) — uses Red active accent per Hi-Fi 06
        RtkSectionHeader(title = "MAX RECORDING DURATION LIMIT (VIDEO MODE)")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MaxVideoDurationOption.entries.forEach { option ->
                RtkSelectableChip(
                    label = option.chipLabel,
                    selected = state.maxVideoDuration == option,
                    onClick = { onUpdateState { it.copy(maxVideoDuration = option) } },
                    accent = ChipAccent.RED
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Shutter Trigger Countdown Timer
        RtkSectionHeader(title = "SHUTTER TRIGGER COUNTDOWN TIMER")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CountdownTimerOption.entries.forEach { option ->
                RtkSelectableChip(
                    label = option.chipLabel,
                    selected = state.countdownTimer == option,
                    onClick = { onUpdateState { it.copy(countdownTimer = option) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Dynamic Estimated Session Info Banner
        RtkInfoBanner(
            icon = RtkIcons.Timer,
            text = state.estimatedSessionText
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Footer Actions
        RtkSheetFooterButtons(
            secondaryLabel = "Reset to Defaults",
            primaryLabel = "Arm Automation",
            onSecondaryClick = onReset,
            onPrimaryClick = onArm,
            secondaryWeight = 0.45f,
            primaryWeight = 0.55f
        )
    }
}
