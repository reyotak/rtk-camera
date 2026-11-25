package com.rtkcamera.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Lightweight Material-style 24dp vector icons matching the Penpot Hi-Fi HUD boards.
 */
object RtkIcons {

    val Flash: ImageVector by lazy {
        materialIcon("Flash") {
            moveTo(7f, 2f)
            verticalLineToRelative(11f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(9f)
            lineToRelative(7f, -12f)
            horizontalLineToRelative(-4f)
            lineToRelative(4f, -8f)
            close()
        }
    }

    val FlashOff: ImageVector by lazy {
        materialIcon("FlashOff") {
            moveTo(3.27f, 3f)
            lineTo(2f, 4.27f)
            lineToRelative(5f, 5f)
            verticalLineTo(13f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(9f)
            lineToRelative(3.58f, -6.14f)
            lineTo(17.73f, 20f)
            lineTo(19f, 18.73f)
            lineTo(3.27f, 3f)
            close()
            moveTo(17f, 10f)
            horizontalLineToRelative(-4f)
            lineToRelative(4f, -8f)
            horizontalLineTo(7f)
            verticalLineToRelative(2.18f)
            lineToRelative(8.46f, 8.46f)
            lineTo(17f, 10f)
            close()
        }
    }

    val FlashAuto: ImageVector by lazy {
        materialIcon("FlashAuto") {
            moveTo(3f, 2f)
            verticalLineToRelative(12f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(9f)
            lineToRelative(7f, -12f)
            horizontalLineTo(9f)
            lineToRelative(4f, -9f)
            horizontalLineTo(3f)
            close()
            moveTo(19f, 2f)
            horizontalLineToRelative(-2f)
            lineToRelative(-3.2f, 9f)
            horizontalLineToRelative(1.9f)
            lineToRelative(0.7f, -2f)
            horizontalLineToRelative(3.2f)
            lineToRelative(0.7f, 2f)
            horizontalLineToRelative(1.9f)
            lineTo(19f, 2f)
            close()
            moveTo(16.85f, 7.65f)
            lineTo(18f, 4f)
            lineToRelative(1.15f, 3.65f)
            horizontalLineToRelative(-2.3f)
            close()
        }
    }

    val WbSunny: ImageVector by lazy {
        materialIcon("WbSunny") {
            moveTo(6.76f, 4.84f)
            lineToRelative(-1.8f, -1.79f)
            lineToRelative(-1.41f, 1.41f)
            lineToRelative(1.79f, 1.79f)
            lineToRelative(1.42f, -1.41f)
            close()
            moveTo(4f, 10.5f)
            horizontalLineTo(1f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(-2f)
            close()
            moveTo(13f, 0.55f)
            horizontalLineToRelative(-2f)
            verticalLineTo(3.5f)
            horizontalLineToRelative(2f)
            verticalLineTo(0.55f)
            close()
            moveTo(20.45f, 4.46f)
            lineToRelative(-1.41f, -1.41f)
            lineToRelative(-1.79f, 1.79f)
            lineToRelative(1.41f, 1.41f)
            lineToRelative(1.79f, -1.79f)
            close()
            moveTo(17.24f, 18.16f)
            lineToRelative(1.79f, 1.8f)
            lineToRelative(1.41f, -1.41f)
            lineToRelative(-1.8f, -1.79f)
            lineToRelative(-1.4f, 1.4f)
            close()
            moveTo(20f, 10.5f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(-3f)
            close()
            moveTo(12f, 5.5f)
            curveToRelative(-3.31f, 0f, -6f, 2.69f, -6f, 6f)
            reflectiveCurveToRelative(2.69f, 6f, 6f, 6f)
            reflectiveCurveToRelative(6f, -2.69f, 6f, -6f)
            reflectiveCurveToRelative(-2.69f, -6f, -6f, -6f)
            close()
            moveTo(11f, 22.45f)
            horizontalLineToRelative(2f)
            verticalLineTo(19.5f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(2.95f)
            close()
            moveTo(3.55f, 18.54f)
            lineToRelative(1.41f, 1.41f)
            lineToRelative(1.79f, -1.8f)
            lineToRelative(-1.41f, -1.41f)
            lineToRelative(-1.79f, 1.8f)
            close()
        }
    }

    val Cloud: ImageVector by lazy {
        materialIcon("Cloud") {
            moveTo(19.35f, 10.04f)
            curveTo(18.67f, 6.59f, 15.64f, 4f, 12f, 4f)
            curveTo(9.11f, 4f, 6.6f, 5.64f, 5.35f, 8.04f)
            curveTo(2.34f, 8.36f, 0f, 10.91f, 0f, 14f)
            curveToRelative(0f, 3.31f, 2.69f, 6f, 6f, 6f)
            horizontalLineToRelative(13f)
            curveToRelative(2.76f, 0f, 5f, -2.24f, 5f, -5f)
            curveToRelative(0f, -2.64f, -2.05f, -4.78f, -4.65f, -4.96f)
            close()
        }
    }

    val Lightbulb: ImageVector by lazy {
        materialIcon("Lightbulb") {
            moveTo(9f, 21f)
            curveToRelative(0f, 0.55f, 0.45f, 1f, 1f, 1f)
            horizontalLineToRelative(4f)
            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
            verticalLineToRelative(-1f)
            horizontalLineTo(9f)
            verticalLineToRelative(1f)
            close()
            moveTo(12f, 2f)
            curveTo(8.14f, 2f, 5f, 5.14f, 5f, 9f)
            curveToRelative(0f, 2.38f, 1.19f, 4.47f, 3f, 5.74f)
            verticalLineTo(17f)
            curveToRelative(0f, 0.55f, 0.45f, 1f, 1f, 1f)
            horizontalLineToRelative(6f)
            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
            verticalLineToRelative(-2.26f)
            curveToRelative(1.81f, -1.27f, 3f, -3.36f, 3f, -5.74f)
            curveToRelative(0f, -3.86f, -3.14f, -7f, -7f, -7f)
            close()
        }
    }

    val CenterFocus: ImageVector by lazy {
        materialIcon("CenterFocus") {
            moveTo(12f, 8f)
            curveToRelative(-2.21f, 0f, -4f, 1.79f, -4f, 4f)
            reflectiveCurveToRelative(1.79f, 4f, 4f, 4f)
            reflectiveCurveToRelative(4f, -1.79f, 4f, -4f)
            reflectiveCurveToRelative(-1.79f, -4f, -4f, -4f)
            close()
            moveTo(5f, 15f)
            horizontalLineTo(3f)
            verticalLineToRelative(4f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(4f)
            verticalLineToRelative(-2f)
            horizontalLineTo(5f)
            verticalLineToRelative(-4f)
            close()
            moveTo(5f, 5f)
            horizontalLineToRelative(4f)
            verticalLineTo(3f)
            horizontalLineTo(5f)
            curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
            verticalLineToRelative(4f)
            horizontalLineToRelative(2f)
            verticalLineTo(5f)
            close()
            moveTo(19f, 3f)
            horizontalLineToRelative(-4f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(4f)
            verticalLineToRelative(4f)
            horizontalLineToRelative(2f)
            verticalLineTo(5f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            close()
            moveTo(19f, 19f)
            horizontalLineToRelative(-4f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(4f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            verticalLineToRelative(-4f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(4f)
            close()
        }
    }

    val Settings: ImageVector by lazy {
        materialIcon("Settings") {
            moveTo(19.14f, 12.94f)
            curveToRelative(0.04f, -0.3f, 0.06f, -0.61f, 0.06f, -0.94f)
            curveToRelative(0f, -0.32f, -0.02f, -0.64f, -0.07f, -0.94f)
            lineToRelative(2.03f, -1.58f)
            curveToRelative(0.18f, -0.14f, 0.23f, -0.41f, 0.12f, -0.61f)
            lineToRelative(-1.92f, -3.32f)
            curveToRelative(-0.12f, -0.22f, -0.37f, -0.29f, -0.59f, -0.22f)
            lineToRelative(-2.39f, 0.96f)
            curveToRelative(-0.5f, -0.38f, -1.03f, -0.7f, -1.62f, -0.94f)
            lineToRelative(-0.36f, -2.54f)
            curveToRelative(-0.04f, -0.24f, -0.24f, -0.41f, -0.48f, -0.41f)
            horizontalLineToRelative(-3.84f)
            curveToRelative(-0.24f, 0f, -0.43f, 0.17f, -0.47f, 0.41f)
            lineToRelative(-0.36f, 2.54f)
            curveToRelative(-0.59f, 0.24f, -1.13f, 0.57f, -1.62f, 0.94f)
            lineToRelative(-2.39f, -0.96f)
            curveToRelative(-0.22f, -0.08f, -0.47f, 0f, -0.59f, 0.22f)
            lineTo(2.74f, 8.87f)
            curveToRelative(-0.12f, 0.21f, -0.08f, 0.47f, 0.12f, 0.61f)
            lineToRelative(2.03f, 1.58f)
            curveToRelative(-0.05f, 0.3f, -0.09f, 0.63f, -0.09f, 0.94f)
            reflectiveCurveToRelative(0.02f, 0.64f, 0.07f, 0.94f)
            lineToRelative(-2.03f, 1.58f)
            curveToRelative(-0.18f, 0.14f, -0.23f, 0.41f, -0.12f, 0.61f)
            lineToRelative(1.92f, 3.32f)
            curveToRelative(0.12f, 0.22f, 0.37f, 0.29f, 0.59f, 0.22f)
            lineToRelative(2.39f, -0.96f)
            curveToRelative(0.5f, 0.38f, 1.03f, 0.7f, 1.62f, 0.94f)
            lineToRelative(0.36f, 2.54f)
            curveToRelative(0.05f, 0.24f, 0.24f, 0.41f, 0.48f, 0.41f)
            horizontalLineToRelative(3.84f)
            curveToRelative(0.24f, 0f, 0.44f, -0.17f, 0.47f, -0.41f)
            lineToRelative(0.36f, -2.54f)
            curveToRelative(0.59f, -0.24f, 1.13f, -0.56f, 1.62f, -0.94f)
            lineToRelative(2.39f, 0.96f)
            curveToRelative(0.22f, 0.08f, 0.47f, 0f, 0.59f, -0.22f)
            lineToRelative(1.92f, -3.32f)
            curveToRelative(0.12f, -0.22f, 0.07f, -0.47f, -0.12f, -0.61f)
            lineTo(19.14f, 12.94f)
            close()
            moveTo(12f, 15.6f)
            curveToRelative(-1.98f, 0f, -3.6f, -1.62f, -3.6f, -3.6f)
            reflectiveCurveToRelative(1.62f, -3.6f, 3.6f, -3.6f)
            reflectiveCurveToRelative(3.6f, 1.62f, 3.6f, 3.6f)
            reflectiveCurveToRelative(-1.62f, 3.6f, -3.6f, 3.6f)
            close()
        }
    }

    val Videocam: ImageVector by lazy {
        materialIcon("Videocam") {
            moveTo(17f, 10.5f)
            verticalLineTo(7f)
            curveToRelative(0f, -0.55f, -0.45f, -1f, -1f, -1f)
            horizontalLineTo(4f)
            curveToRelative(-0.55f, 0f, -1f, 0.45f, -1f, 1f)
            verticalLineToRelative(10f)
            curveToRelative(0f, 0.55f, 0.45f, 1f, 1f, 1f)
            horizontalLineToRelative(12f)
            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
            verticalLineToRelative(-3.5f)
            lineToRelative(4f, 4f)
            verticalLineToRelative(-11f)
            lineToRelative(-4f, 4f)
            close()
        }
    }

    val PhotoCamera: ImageVector by lazy {
        materialIcon("PhotoCamera") {
            moveTo(9f, 2f)
            lineTo(7.17f, 4f)
            horizontalLineTo(4f)
            curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
            verticalLineToRelative(12f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(16f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            verticalLineTo(6f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            horizontalLineToRelative(-3.17f)
            lineTo(15f, 2f)
            horizontalLineTo(9f)
            close()
            moveTo(12f, 17f)
            curveToRelative(-2.76f, 0f, -5f, -2.24f, -5f, -5f)
            reflectiveCurveToRelative(2.24f, -5f, 5f, -5f)
            reflectiveCurveToRelative(5f, 2.24f, 5f, 5f)
            reflectiveCurveToRelative(-2.24f, 5f, -5f, 5f)
            close()
        }
    }

    val Tune: ImageVector by lazy {
        materialIcon("Tune") {
            moveTo(3f, 17f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(6f)
            verticalLineToRelative(-2f)
            horizontalLineTo(3f)
            close()
            moveTo(3f, 5f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(10f)
            verticalLineTo(5f)
            horizontalLineTo(3f)
            close()
            moveTo(13f, 21f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(8f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(-8f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(6f)
            horizontalLineToRelative(2f)
            close()
            moveTo(7f, 9f)
            verticalLineToRelative(2f)
            horizontalLineTo(3f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(4f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(2f)
            verticalLineTo(9f)
            horizontalLineTo(7f)
            close()
            moveTo(21f, 13f)
            verticalLineToRelative(-2f)
            horizontalLineTo(11f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(10f)
            close()
            moveTo(15f, 9f)
            horizontalLineToRelative(2f)
            verticalLineTo(7f)
            horizontalLineToRelative(4f)
            verticalLineTo(5f)
            horizontalLineToRelative(-4f)
            verticalLineTo(3f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(6f)
            close()
        }
    }

    val Timer: ImageVector by lazy {
        materialIcon("Timer") {
            moveTo(15f, 1f)
            horizontalLineTo(9f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(6f)
            verticalLineTo(1f)
            close()
            moveTo(11f, 14f)
            horizontalLineToRelative(2f)
            verticalLineTo(8f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(6f)
            close()
            moveTo(19.03f, 7.39f)
            lineToRelative(1.42f, -1.42f)
            curveToRelative(-0.43f, -0.51f, -0.9f, -0.99f, -1.41f, -1.41f)
            lineToRelative(-1.42f, 1.42f)
            curveTo(16.07f, 4.74f, 14.12f, 4f, 12f, 4f)
            curveToRelative(-4.97f, 0f, -9f, 4.03f, -9f, 9f)
            reflectiveCurveToRelative(4.02f, 9f, 9f, 9f)
            reflectiveCurveToRelative(9f, -4.03f, 9f, -9f)
            curveToRelative(0f, -2.12f, -0.74f, -4.07f, -1.97f, -5.61f)
            close()
            moveTo(12f, 20f)
            curveToRelative(-3.87f, 0f, -7f, -3.13f, -7f, -7f)
            reflectiveCurveToRelative(3.13f, -7f, 7f, -7f)
            reflectiveCurveToRelative(7f, 3.13f, 7f, 7f)
            reflectiveCurveToRelative(-3.13f, 7f, -7f, 7f)
            close()
        }
    }

    val Gallery: ImageVector by lazy {
        materialIcon("Gallery") {
            moveTo(21f, 19f)
            verticalLineTo(5f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            horizontalLineTo(5f)
            curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
            verticalLineToRelative(14f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(14f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            close()
            moveTo(8.5f, 13.5f)
            lineToRelative(2.5f, 3.01f)
            lineTo(14.5f, 12f)
            lineToRelative(4.5f, 6f)
            horizontalLineTo(5f)
            lineToRelative(3.5f, -4.5f)
            close()
        }
    }

    val CameraSwitch: ImageVector by lazy {
        materialIcon("CameraSwitch") {
            moveTo(12f, 4f)
            verticalLineTo(1f)
            lineTo(8f, 5f)
            lineToRelative(4f, 4f)
            verticalLineTo(6f)
            curveToRelative(3.31f, 0f, 6f, 2.69f, 6f, 6f)
            curveToRelative(0f, 1.01f, -0.25f, 1.97f, -0.7f, 2.8f)
            lineToRelative(1.46f, 1.46f)
            curveTo(19.54f, 15.03f, 20f, 13.57f, 20f, 12f)
            curveToRelative(0f, -4.42f, -3.58f, -8f, -8f, -8f)
            close()
            moveTo(12f, 18f)
            curveToRelative(-3.31f, 0f, -6f, -2.69f, -6f, -6f)
            curveToRelative(0f, -1.01f, 0.25f, -1.97f, 0.7f, -2.8f)
            lineTo(5.24f, 7.74f)
            curveTo(4.46f, 8.97f, 4f, 10.43f, 4f, 12f)
            curveToRelative(0f, 4.42f, 3.58f, 8f, 8f, 8f)
            verticalLineToRelative(3f)
            lineToRelative(4f, -4f)
            lineToRelative(-4f, -4f)
            verticalLineToRelative(3f)
            close()
        }
    }

    val Pause: ImageVector by lazy {
        materialIcon("Pause") {
            moveTo(6f, 19f)
            horizontalLineToRelative(4f)
            verticalLineTo(5f)
            horizontalLineTo(6f)
            verticalLineToRelative(14f)
            close()
            moveTo(14f, 5f)
            verticalLineToRelative(14f)
            horizontalLineToRelative(4f)
            verticalLineTo(5f)
            horizontalLineToRelative(-4f)
            close()
        }
    }

    val PlayArrow: ImageVector by lazy {
        materialIcon("PlayArrow") {
            moveTo(8f, 5f)
            verticalLineToRelative(14f)
            lineToRelative(11f, -7f)
            close()
        }
    }

    val Close: ImageVector by lazy {
        materialIcon("Close") {
            moveTo(19f, 6.41f)
            lineTo(17.59f, 5f)
            lineTo(12f, 10.59f)
            lineTo(6.41f, 5f)
            lineTo(5f, 6.41f)
            lineTo(10.59f, 12f)
            lineTo(5f, 17.59f)
            lineTo(6.41f, 19f)
            lineTo(12f, 13.41f)
            lineTo(17.59f, 19f)
            lineTo(19f, 17.59f)
            lineTo(13.41f, 12f)
            close()
        }
    }

    val ArrowBack: ImageVector by lazy {
        materialIcon("ArrowBack") {
            moveTo(20f, 11f)
            horizontalLineTo(7.83f)
            lineToRelative(5.59f, -5.59f)
            lineTo(12f, 4f)
            lineToRelative(-8f, 8f)
            lineToRelative(8f, 8f)
            lineToRelative(1.41f, -1.41f)
            lineTo(7.83f, 13f)
            horizontalLineTo(20f)
            verticalLineToRelative(-2f)
            close()
        }
    }

    val NorthEast: ImageVector by lazy {
        materialIcon("NorthEast") {
            moveTo(9f, 5f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(6.59f)
            lineTo(4f, 18.59f)
            lineTo(5.41f, 20f)
            lineTo(17f, 8.41f)
            verticalLineTo(15f)
            horizontalLineToRelative(2f)
            verticalLineTo(5f)
            horizontalLineTo(9f)
            close()
        }
    }

    val Warning: ImageVector by lazy {
        materialIcon("Warning") {
            moveTo(1f, 21f)
            horizontalLineToRelative(22f)
            lineTo(12f, 2f)
            lineTo(1f, 21f)
            close()
            moveTo(13f, 18f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            close()
            moveTo(13f, 14f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-4f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(4f)
            close()
        }
    }

    val Check: ImageVector by lazy {
        materialIcon("Check") {
            moveTo(9f, 16.17f)
            lineTo(4.83f, 12f)
            lineToRelative(-1.42f, 1.41f)
            lineTo(9f, 19f)
            lineTo(21f, 7f)
            lineToRelative(-1.41f, -1.41f)
            close()
        }
    }

    val Info: ImageVector by lazy {
        materialIcon("Info") {
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            reflectiveCurveToRelative(4.48f, 10f, 10f, 10f)
            reflectiveCurveToRelative(10f, -4.48f, 10f, -10f)
            reflectiveCurveTo(17.52f, 2f, 12f, 2f)
            close()
            moveTo(13f, 17f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(-6f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(6f)
            close()
            moveTo(13f, 9f)
            horizontalLineToRelative(-2f)
            verticalLineTo(7f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            close()
        }
    }

    val ArrowDropDown: ImageVector by lazy {
        materialIcon("ArrowDropDown") {
            moveTo(7f, 10f)
            lineToRelative(5f, 5f)
            lineToRelative(5f, -5f)
            close()
        }
    }

    val Help: ImageVector by lazy {
        materialIcon("Help") {
            moveTo(11f, 18f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(-2f)
            horizontalLineToRelative(-2f)
            verticalLineToRelative(2f)
            close()
            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            reflectiveCurveToRelative(4.48f, 10f, 10f, 10f)
            reflectiveCurveToRelative(10f, -4.48f, 10f, -10f)
            reflectiveCurveTo(17.52f, 2f, 12f, 2f)
            close()
            moveTo(12f, 20f)
            curveToRelative(-4.41f, 0f, -8f, -3.59f, -8f, -8f)
            reflectiveCurveToRelative(3.59f, -8f, 8f, -8f)
            reflectiveCurveToRelative(8f, 3.59f, 8f, 8f)
            reflectiveCurveToRelative(-3.59f, 8f, -8f, 8f)
            close()
            moveTo(12f, 6f)
            curveToRelative(-2.21f, 0f, -4f, 1.79f, -4f, 4f)
            horizontalLineToRelative(2f)
            curveToRelative(0f, -1.1f, 0.9f, -2f, 2f, -2f)
            reflectiveCurveToRelative(2f, 0.9f, 2f, 2f)
            curveToRelative(0f, 2f, -3f, 1.75f, -3f, 5f)
            horizontalLineToRelative(2f)
            curveToRelative(0f, -2.25f, 3f, -2.5f, 3f, -5f)
            curveToRelative(0f, -2.21f, -1.79f, -4f, -4f, -4f)
            close()
        }
    }

    val ChevronRight: ImageVector by lazy {
        materialIcon("ChevronRight") {
            moveTo(10f, 6f)
            lineTo(8.59f, 7.41f)
            lineTo(13.17f, 12f)
            lineToRelative(-4.58f, 4.59f)
            lineTo(10f, 18f)
            lineToRelative(6f, -6f)
            close()
        }
    }

    val MenuBook: ImageVector by lazy {
        materialIcon("MenuBook") {
            moveTo(21f, 5f)
            curveToRelative(-1.11f, -0.35f, -2.33f, -0.5f, -3.5f, -0.5f)
            curveToRelative(-1.95f, 0f, -4.05f, 0.4f, -5.5f, 1.5f)
            curveToRelative(-1.45f, -1.1f, -3.55f, -1.5f, -5.5f, -1.5f)
            reflectiveCurveTo(2.45f, 4.9f, 1f, 6f)
            verticalLineToRelative(14.65f)
            curveToRelative(0f, 0.25f, 0.25f, 0.5f, 0.5f, 0.5f)
            curveToRelative(0.1f, 0f, 0.15f, -0.05f, 0.25f, -0.05f)
            curveTo(3.1f, 20.45f, 5.05f, 20f, 6.5f, 20f)
            curveToRelative(1.95f, 0f, 4.05f, 0.4f, 5.5f, 1.5f)
            curveToRelative(1.35f, -0.85f, 3.8f, -1.5f, 5.5f, -1.5f)
            curveToRelative(1.65f, 0f, 3.35f, 0.3f, 4.75f, 1.05f)
            curveToRelative(0.1f, 0.05f, 0.15f, 0.05f, 0.25f, 0.05f)
            curveToRelative(0.25f, 0f, 0.5f, -0.25f, 0.5f, -0.5f)
            verticalLineTo(6f)
            curveToRelative(-0.6f, -0.45f, -1.25f, -0.75f, -2f, -1f)
            close()
            moveTo(21f, 18.5f)
            curveToRelative(-1.1f, -0.35f, -2.3f, -0.5f, -3.5f, -0.5f)
            curveToRelative(-1.7f, 0f, -4.15f, 0.65f, -5.5f, 1.5f)
            verticalLineTo(8f)
            curveToRelative(1.35f, -0.85f, 3.8f, -1.5f, 5.5f, -1.5f)
            curveToRelative(1.2f, 0f, 2.4f, 0.15f, 3.5f, 0.5f)
            verticalLineToRelative(11.5f)
            close()
        }
    }

    private fun materialIcon(
        name: String,
        block: PathBuilder.() -> Unit
    ): ImageVector = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.EvenOdd,
            pathBuilder = block
        )
    }.build()
}
