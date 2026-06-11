package com.rtkcamera.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rtkcamera.nativebridge.AlgorithmInfo

/**
 * Menu to select from available native algorithms.
 * Satisfies User Story 4: Algorithm Management & Comparison.
 */
@Composable
fun AlgorithmMenu(
    algorithms: List<AlgorithmInfo>,
    selectedId: String?,
    onSelected: (AlgorithmInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(16.dp)) {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = "Switch Algorithm")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            algorithms.forEach { algo ->
                DropdownMenuItem(
                    text = { Text(algo.name) },
                    onClick = {
                        onSelected(algo)
                        expanded = false
                    },
                    trailingIcon = {
                        if (algo.id == selectedId) {
                            RadioButton(selected = true, onClick = null)
                        }
                    }
                )
            }
        }
    }
}
