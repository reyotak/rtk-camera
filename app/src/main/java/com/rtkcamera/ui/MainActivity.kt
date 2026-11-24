package com.rtkcamera.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.rtkcamera.ui.theme.RtkCameraTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Entry point for the RTK Camera application.
 * Uses Hilt for dependency injection (AndroidEntryPoint).
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: CameraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.BLACK)
        )

        setContent {
            RtkCameraTheme {
                ViewfinderScreen(viewModel = viewModel)
            }
        }
    }
}
