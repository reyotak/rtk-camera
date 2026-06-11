package com.rtkcamera.ui

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rtkcamera.camera.AnalysisPipeline
import com.rtkcamera.ui.components.AlgorithmMenu

/**
 * Main Viewfinder screen using Compose and CameraX.
 */
@Composable
fun ViewfinderScreen(
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    Box(modifier = modifier.fillMaxSize()) {
        // Camera Preview
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Algorithm Selection Menu (User Story 4)
        AlgorithmMenu(
            algorithms = uiState.availableAlgorithms,
            selectedId = uiState.activeAlgorithmId,
            onSelected = { viewModel.onAlgorithmSelected(it) },
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 48.dp)
        )

        // Performance Warning Overlay (Principle III)
        val warning = uiState.performanceWarning
        if (warning != null) {
            Surface(
                color = Color.Red.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = warning,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // State info and processing indicator
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isProcessing) {
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Manual Capture Trigger (User Story 3)
            Button(
                onClick = { viewModel.onCaptureTriggered() },
                modifier = Modifier.size(72.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.8f))
            ) {
                // Outer circle for the "shutter" look
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Active Algorithm: ${uiState.activeAlgorithmId ?: "None"}",
                    color = Color.White,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
