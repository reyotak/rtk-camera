package com.rtkcamera.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import com.rtkcamera.nativebridge.AlgorithmInfo
import com.rtkcamera.ui.AlgorithmTuningState
import com.rtkcamera.ui.GaussianKernelSize
import com.rtkcamera.ui.theme.RtkColorTokens
import kotlin.math.roundToInt

/**
 * Hi-Fi 02 • Algorithm Sandbox & Tuning Drawer.
 */
@Composable
fun AlgorithmTuningSheet(
    state: AlgorithmTuningState,
    availableAlgorithms: List<AlgorithmInfo>,
    onUpdateState: ((AlgorithmTuningState) -> AlgorithmTuningState) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val modules: List<Pair<String, String>> = if (availableAlgorithms.isNotEmpty()) {
        availableAlgorithms.map { it.id to it.name }
    } else {
        AlgorithmTuningState.DefaultModules
    }

    RtkBottomSheetContainer(
        title = "Algorithm Sandbox & Tuning",
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        // AVAILABLE MODULES
        RtkSectionHeader(title = "AVAILABLE MODULES")
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            modules.forEach { (id, name) ->
                RtkSelectableChip(
                    label = name,
                    selected = state.selectedModuleId == id,
                    onClick = {
                        onUpdateState {
                            it.copy(selectedModuleId = id, selectedModuleName = name)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Low Threshold (Hysteresis)
        SliderHeaderRow(
            title = "Low Threshold (Hysteresis)",
            valueText = "${state.lowThreshold} / 255"
        )
        Spacer(modifier = Modifier.height(6.dp))
        RtkParameterSlider(
            value = state.lowThreshold / 255f,
            onValueChange = { fraction ->
                onUpdateState { it.copy(lowThreshold = (fraction * 255f).roundToInt()) }
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // High Threshold (Gradient Limit)
        SliderHeaderRow(
            title = "High Threshold (Gradient Limit)",
            valueText = "${state.highThreshold} / 255"
        )
        Spacer(modifier = Modifier.height(6.dp))
        RtkParameterSlider(
            value = state.highThreshold / 255f,
            onValueChange = { fraction ->
                onUpdateState { it.copy(highThreshold = (fraction * 255f).roundToInt()) }
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Gaussian Blur Kernel Size
        Text(
            text = "Gaussian Blur Kernel Size",
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = RtkColorTokens.TextPrimary
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GaussianKernelSize.entries.forEach { kernel ->
                RtkSelectableChip(
                    label = kernel.label,
                    selected = state.kernelSize == kernel,
                    onClick = { onUpdateState { it.copy(kernelSize = kernel) } },
                    cornerRadius = 8.dp,
                    showActiveDot = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save RAW Frame with Snapshot toggle card
        RtkToggleRowCard(
            title = "Save RAW Frame with Snapshot",
            subtitle = "Saves uncompressed DNG alongside processed JPEG",
            checked = state.saveRawWithSnapshot,
            onToggle = {
                onUpdateState { it.copy(saveRawWithSnapshot = !it.saveRawWithSnapshot) }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Footer actions
        RtkSheetFooterButtons(
            secondaryLabel = "Reset",
            primaryLabel = "Apply to Live Stream",
            onSecondaryClick = onReset,
            onPrimaryClick = onApply,
            secondaryWeight = 0.34f,
            primaryWeight = 0.66f
        )
    }
}

@Composable
private fun SliderHeaderRow(
    title: String,
    valueText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = RtkColorTokens.TextPrimary
            )
        )
        Text(
            text = valueText,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = RtkColorTokens.AccentCyan
            )
        )
    }
}

@Composable
private fun RtkCyanToggleSwitch(checked: Boolean) {
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
