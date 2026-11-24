package com.rtkcamera.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Semantic Color Tokens matching Penpot "02. Design System & Foundations"
 * and Hi-Fi 01a–06 boards.
 */
object RtkColorTokens {
    // Canvas & Surfaces
    val CanvasBlack = Color(0xFF000000)
    val CanvasDeep = Color(0xFF06080C)
    val CanvasDark = Color(0xFF0A0D12)
    val SurfacePillDark = Color(0xFF0A0E17)
    val SurfaceZoomIdle = Color(0xFF0A0D14)
    val SurfaceModeToggle = Color(0xFF0E1420)
    val SurfaceSheet = Color(0xFF0F141D)
    val SurfaceControl = Color(0xFF10141D)
    val SurfaceInfoBox = Color(0xFF101622)
    val SurfacePreviewPlaceholder = Color(0xFF121A2B)
    val SurfaceCard = Color(0xFF131822)
    val SurfaceToggleCard = Color(0xFF131A26)
    val SurfaceAlgoPill = Color(0xFF131C2C)
    val SurfaceMetaCard = Color(0xFF141A24)
    val SurfaceActionButton = Color(0xFF141C2B)
    val SurfaceDeckButton = Color(0xFF161E2E)
    val SurfacePauseButton = Color(0xFF16202E)
    val SurfaceChipActiveCyan = Color(0xFF16283C)
    val SurfaceButtonSecondary = Color(0xFF1A2332)
    val SurfaceChipIdle = Color(0xFF1E293B)

    // Semantic Status Containers
    val SurfaceRecordingPill = Color(0xFF2A0A10)
    val SurfaceLimitActiveRed = Color(0xFF2A1724)
    val SurfaceWarningCard = Color(0xFF1F0E14)
    val ScrimSheetBackdrop = Color(0xE60E1522)

    // Borders
    val BorderSheet = Color(0xFF222D3E)
    val BorderSubtle = Color(0xFF232D3F)
    val BorderControl = Color(0xFF2A364F)
    val BorderSecondary = Color(0xFF2A374A)
    val BorderAction = Color(0xFF2A3A54)
    val BorderMuted = Color(0xFF334155)

    // Accents
    val AccentCyan = Color(0xFF00E5FF)
    val AccentMint = Color(0xFF00E676)
    val AccentAmber = Color(0xFFFFB300)
    val AccentRed = Color(0xFFFF1744)
    val AccentRedSoft = Color(0xFFFF5252)
    val AccentError = Color(0xFFFF3D00)

    // Text
    val TextPrimary = Color(0xFFFFFFFF)
    val TextZoomIdle = Color(0xFFE2E8F0)
    val TextSecondary = Color(0xFFCBD5E1)
    val TextMuted = Color(0xFF94A3B8)
    val TextOnAccent = Color(0xFF0A0D12)
}

@Immutable
data class RtkExtendedColors(
    val canvasDeep: Color = RtkColorTokens.CanvasDeep,
    val canvasDark: Color = RtkColorTokens.CanvasDark,
    val surfaceSheet: Color = RtkColorTokens.SurfaceSheet,
    val surfaceCard: Color = RtkColorTokens.SurfaceCard,
    val surfaceControl: Color = RtkColorTokens.SurfaceControl,
    val surfaceDeckButton: Color = RtkColorTokens.SurfaceDeckButton,
    val surfaceActionButton: Color = RtkColorTokens.SurfaceActionButton,
    val surfaceChipIdle: Color = RtkColorTokens.SurfaceChipIdle,
    val surfaceChipActiveCyan: Color = RtkColorTokens.SurfaceChipActiveCyan,
    val surfaceLimitActiveRed: Color = RtkColorTokens.SurfaceLimitActiveRed,
    val surfaceRecordingPill: Color = RtkColorTokens.SurfaceRecordingPill,
    val surfaceWarningCard: Color = RtkColorTokens.SurfaceWarningCard,
    val surfaceMetaCard: Color = RtkColorTokens.SurfaceMetaCard,
    val surfaceInfoBox: Color = RtkColorTokens.SurfaceInfoBox,
    val borderSheet: Color = RtkColorTokens.BorderSheet,
    val borderSubtle: Color = RtkColorTokens.BorderSubtle,
    val borderControl: Color = RtkColorTokens.BorderControl,
    val borderMuted: Color = RtkColorTokens.BorderMuted,
    val accentCyan: Color = RtkColorTokens.AccentCyan,
    val accentMint: Color = RtkColorTokens.AccentMint,
    val accentAmber: Color = RtkColorTokens.AccentAmber,
    val accentRed: Color = RtkColorTokens.AccentRed,
    val accentRedSoft: Color = RtkColorTokens.AccentRedSoft,
    val accentError: Color = RtkColorTokens.AccentError,
    val textPrimary: Color = RtkColorTokens.TextPrimary,
    val textSecondary: Color = RtkColorTokens.TextSecondary,
    val textMuted: Color = RtkColorTokens.TextMuted,
    val textOnAccent: Color = RtkColorTokens.TextOnAccent
)
