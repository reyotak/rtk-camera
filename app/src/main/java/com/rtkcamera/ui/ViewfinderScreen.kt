package com.rtkcamera.ui

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionResult(isGranted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.hasCameraPermission) {
            val cameraProvider = viewModel.cameraProviderFlow.collectAsStateWithLifecycle().value
            if (cameraProvider != null) {
                // Camera Preview
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { previewView ->
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview
                            )
                        } catch (exc: Exception) {
                            Log.e("ViewfinderScreen", "Use case binding failed", exc)
                            viewModel.onCameraError("Binding failed")
                        }
                    }
                )
            } else if (uiState.cameraError == null) {
                // Loading provider
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }

        // Camera Error Fallback UI
        uiState.cameraError?.let { errorMsg ->
            Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Surface(color = Color.Black.copy(alpha = 0.7f), shape = MaterialTheme.shapes.medium) {
                    Text(text = errorMsg, color = Color.White, modifier = Modifier.padding(16.dp))
                }
            }
        }

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
