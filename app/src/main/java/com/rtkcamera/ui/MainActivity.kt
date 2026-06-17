package com.rtkcamera.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * Entry point for the RTK Camera application.
 * Uses Hilt for dependency injection (AndroidEntryPoint).
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: CameraViewModel by viewModels()

    @javax.inject.Inject
    lateinit var capturePipeline: com.rtkcamera.camera.CapturePipeline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            // Root UI: Viewfinder with Algorithm Sandbox controls
            ViewfinderScreen(viewModel = viewModel, capturePipeline = capturePipeline)
        }
    }
}
