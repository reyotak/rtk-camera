package com.rtkcamera.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtkcamera.ui.icons.RtkIcons
import com.rtkcamera.ui.theme.RtkColorTokens

/**
 * Hi-Fi 04 • High-Res Snapshot Review ("Snapshot Inspector").
 */
@Composable
fun SnapshotInspectorScreen(
    metadata: SnapshotMetadata,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onOpenInGalleryClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RtkColorTokens.CanvasDeep)
    ) {
        // Top Header Bar (with 48x48dp accessible hit targets)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RtkColorTokens.CanvasBlack.copy(alpha = 0.85f))
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onBackClick),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = RtkColorTokens.SurfaceChipIdle.copy(alpha = 0.9f),
                        shape = CircleShape,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = RtkIcons.ArrowBack,
                                contentDescription = "Back to Viewfinder",
                                tint = RtkColorTokens.TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Snapshot Inspector",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = RtkColorTokens.TextPrimary
                    )
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onShareClick),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = RtkColorTokens.SurfaceChipIdle.copy(alpha = 0.9f),
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = RtkIcons.NorthEast,
                            contentDescription = "Open External",
                            tint = RtkColorTokens.TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // High-Res Processed Preview Area (matches Penpot geometric Canny wireframe placeholder)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(RtkColorTokens.SurfacePreviewPlaceholder)
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            // Saved Confirmation Pill
            Surface(
                color = RtkColorTokens.CanvasBlack.copy(alpha = 0.75f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.2.dp, RtkColorTokens.AccentMint),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .height(32.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = RtkIcons.Check,
                        contentDescription = null,
                        tint = RtkColorTokens.AccentMint,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = metadata.savedBannerText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RtkColorTokens.AccentMint
                        )
                    )
                }
            }

            // Edge-Detected Frame Placeholder Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp, bottom = 12.dp, start = 6.dp, end = 6.dp)
            ) {
                val strokePx = 2.dp.toPx()
                val edgeColor = RtkColorTokens.AccentCyan.copy(alpha = 0.9f)
                // Outer processed frame outline
                drawRect(
                    color = edgeColor,
                    topLeft = Offset.Zero,
                    size = size,
                    style = Stroke(width = strokePx)
                )
                // Inner nested architectural edge box (matching Hi-Fi 04 preview)
                val innerInsetX = size.width * 0.15f
                val innerTop = size.height * 0.30f
                drawRect(
                    color = edgeColor,
                    topLeft = Offset(innerInsetX, innerTop),
                    size = Size(size.width - innerInsetX * 2f, size.height - innerTop),
                    style = Stroke(width = strokePx)
                )
            }
        }

        // Bottom Metadata Sheet
        Surface(
            color = RtkColorTokens.SurfaceSheet,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            border = BorderStroke(1.5.dp, RtkColorTokens.BorderSheet),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(width = 40.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(RtkColorTokens.TextMuted)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = metadata.fileName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RtkColorTokens.TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = metadata.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = RtkColorTokens.TextMuted
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 2x2 Metadata Cards Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SnapshotMetaCard(
                        label = "RESOLUTION",
                        value = metadata.resolutionValue,
                        subtext = metadata.resolutionSubtext,
                        valueColor = RtkColorTokens.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    SnapshotMetaCard(
                        label = "ALGORITHM",
                        value = metadata.algorithmValue,
                        subtext = metadata.algorithmSubtext,
                        valueColor = RtkColorTokens.AccentCyan,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SnapshotMetaCard(
                        label = "EXPOSURE / OPTICS",
                        value = metadata.exposureValue,
                        subtext = metadata.exposureSubtext,
                        valueColor = RtkColorTokens.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    SnapshotMetaCard(
                        label = "STORAGE & FORMAT",
                        value = metadata.storageValue,
                        subtext = metadata.storageSubtext,
                        valueColor = RtkColorTokens.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Footer Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        color = RtkColorTokens.SurfaceButtonSecondary,
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, RtkColorTokens.BorderSecondary),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable(onClick = onExportClick)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Export RAW + Processed",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RtkColorTokens.TextPrimary
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }

                    Surface(
                        color = RtkColorTokens.AccentCyan,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable(onClick = onOpenInGalleryClick)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = "Open in Gallery",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RtkColorTokens.CanvasBlack
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = RtkIcons.NorthEast,
                                contentDescription = null,
                                tint = RtkColorTokens.CanvasBlack,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SnapshotMetaCard(
    label: String,
    value: String,
    subtext: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = RtkColorTokens.SurfaceMetaCard,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, RtkColorTokens.BorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = RtkColorTokens.TextMuted
                )
            )
            Text(
                text = value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
            )
            Text(
                text = subtext,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 9.sp,
                    color = RtkColorTokens.TextMuted
                )
            )
        }
    }
}
