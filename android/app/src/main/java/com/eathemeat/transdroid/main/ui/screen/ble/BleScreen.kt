package com.eathemeat.transdroid.main.ui.screen.ble

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.R
import com.eathemeat.transdroid.main.MainModel
import com.eathemeat.transdroid.main.data.ConnectionType
import com.eathemeat.transdroid.main.data.Device
import com.eathemeat.transdroid.main.data.FileItem
import com.eathemeat.transdroid.main.data.TransferTask
import com.eathemeat.transdroid.main.ui.screen.advertisement.AdScreen
import com.eathemeat.transdroid.main.ui.theme.CyberpunkDarkColors
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun BleScreen(
    modifier: Modifier = Modifier,
    mainModel: MainModel = viewModel(),
) {
    val configuration = LocalConfiguration.current
    val isLargeScreen = configuration.screenWidthDp >= 1024
    val bleModel = mainModel.bleModel
    val uiState by mainModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.radialGradient(CyberpunkDarkColors))
            .padding(
                horizontal = if (isLargeScreen) 32.dp else 16.dp, vertical = 16.dp,
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderSection(
                isConnected = bleModel.connectedDevice != null,
                deviceName = bleModel.connectedDevice?.name ?: "等待连接...",
                onThemeToggle = mainModel::toggleTheme,
                themeMode = uiState.themeMode
            )
            Spacer(modifier = Modifier.height(32.dp))
            MainContentArea(
                uiState = uiState,
                onScanClick = bleModel::scanDevices,
                onConnectDevice = bleModel::connectToDevice,
                onAddFiles = bleModel::onAddFiles,
                onRemoveFile = bleModel::onRemoveFile,
                onClearAllFiles = bleModel::onClearAllFiles,
                onStartTransfer = bleModel::onStartTransfer,
                onRetryTransfer = bleModel::onRetryTransfer,
                onSwitchConnectionType = bleModel::switchConnectionType,
                onShowQrCode = bleModel::showQrCodeModal,
                onToggleHotspot = bleModel::toggleWifiHotspot,
                isLargeScreen = isLargeScreen
            )
        }

}
}

@Composable
private fun MainContentArea(
    uiState: MainModel.TransferUiState,
    onScanClick: () -> Unit,
    onConnectDevice: (Device) -> Unit,
    onAddFiles: (List<FileItem>) -> Unit,
    onRemoveFile: (FileItem) -> Unit,
    onClearAllFiles: () -> Unit,
    onStartTransfer: () -> Unit,
    onRetryTransfer: (TransferTask) -> Unit,
    onSwitchConnectionType: (ConnectionType) -> Unit,
    onShowQrCode: () -> Unit,
    onToggleHotspot: () -> Unit,
    isLargeScreen: Boolean
) {
    val arrangement = if (isLargeScreen) Arrangement.spacedBy(24.dp) else Arrangement.spacedBy(16.dp)
    Row(
        modifier = Modifier
            .fillMaxSize(),
//            .weight(1f),
        horizontalArrangement = arrangement
    ) {
        ConnectionPanel(
            uiState = uiState,
            onScanClick = onScanClick,
            onConnectDevice = onConnectDevice,
            onSwitchConnectionType = onSwitchConnectionType,
            onShowQrCode = onShowQrCode,
            onToggleHotspot = onToggleHotspot,
            modifier = Modifier.weight(if (isLargeScreen) 0.25f else 1f)
        )
        FileSelectionPanel(
            uiState = uiState,
            onAddFiles = onAddFiles,
            onRemoveFile = onRemoveFile,
            onClearAllFiles = onClearAllFiles,
            onStartTransfer = onStartTransfer,
            modifier = Modifier.weight(if (isLargeScreen) 0.5f else 1f)
        )
        TransferQueuePanel(
            uiState = uiState,
            onRetryTransfer = onRetryTransfer,
            modifier = Modifier.weight(if (isLargeScreen) 0.25f else 1f)
        )
    }
}

@Composable
private fun ConnectionPanel(
    uiState: MainModel.TransferUiState,
    onScanClick: () -> Unit,
    onConnectDevice: (Device) -> Unit,
    onSwitchConnectionType: (ConnectionType) -> Unit,
    onShowQrCode: () -> Unit,
    onToggleHotspot: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier.fillMaxHeight()) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text(
                text = "连接中心",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.Black.copy(alpha = 0.2f), shape = RoundedCornerShape(20.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabButton(
                    text = "蓝牙",
                    icon = painterResource(R.drawable.ic_bluetooth),
                    selected = uiState.activeConnectionType == com.example.flashshare.ui.ConnectionType.Bluetooth,
                    onClick = { onSwitchConnectionType(com.example.flashshare.ui.ConnectionType.Bluetooth) }
                )
                TabButton(
                    text = "WiFi",
                    icon = painterResource(R.drawable.ic_wifi),
                    selected = uiState.activeConnectionType == com.example.flashshare.ui.ConnectionType.WiFi,
                    onClick = { onSwitchConnectionType(com.example.flashshare.ui.ConnectionType.WiFi) }
                )
            }

            AnimatedVisibility(visible = uiState.activeConnectionType == com.example.flashshare.ui.ConnectionType.Bluetooth) {
                BluetoothContent(
                    uiState = uiState,
                    onScanClick = onScanClick,
                    onConnectDevice = onConnectDevice
                )
            }

            AnimatedVisibility(visible = uiState.activeConnectionType == com.example.flashshare.ui.ConnectionType.WiFi) {
                WiFiContent(
                    uiState = uiState,
                    onScanClick = onScanClick,
                    onConnectDevice = onConnectDevice,
                    onShowQrCode = onShowQrCode,
                    onToggleHotspot = onToggleHotspot
                )
            }
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    icon: Painter,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
//            .weight(1f)
            .clickable(onClick = onClick)
            .background(
                color = if (selected) Color.White.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(painter = icon, contentDescription = null, tint = if (selected) Color.White else Color.Gray, modifier = Modifier.size(14.dp))
            Text(text = text, fontSize = 12.sp, color = if (selected) Color.White else Color.Gray)
        }
    }
}

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
//            .glassSurface()
            .drawWithCache {
                onDrawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.1f)),
                            startY = 0f,
                            endY = size.height
                        )
                    )
                }
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}


@Composable
private fun HeaderSection(
    isConnected: Boolean,
    deviceName: String,
    onThemeToggle: () -> Unit,
    themeMode: MainModel.ThemeMode
) {
    val floatAnim by animateFloatAsState(
        targetValue = if (isConnected) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 6000
                0f at 0 with LinearEasing
                -10f at 3000 with LinearEasing
                0f at 6000 with LinearEasing
            }
        ),
        label = "floatAnimation"
    )

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(Color(0xFF00F3FF), Color(0xFFBC13FE))),
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
                Column {
                    Text(
                        text = "FlashShare PRO 2026",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.White
                    )
                    Text(
                        text = if (isConnected) "已连接: $deviceName" else "等待连接...",
                        fontSize = 12.sp,
                        color = if (isConnected) Color(0xFF00F3FF) else Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            IconButton(
                onClick = onThemeToggle,
                modifier = Modifier
                    .size(40.dp)
                    .glassSurface()
            ) {
                Icon(
                    imageVector = if (themeMode == ThemeMode.Dark) Icons.Default.WbSunny else Icons.Default.DarkMode,
                    contentDescription = "切换主题",
                    tint = if (themeMode == ThemeMode.Dark) Color(0xFFFFD700) else Color(0xFF00F3FF)
                )
            }
        }
    }


    @Composable
    fun GlassCard(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Card(
            modifier = modifier
                .glassSurface()
                .drawWithCache {
                    onDrawBehind {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.1f)),
                                startY = 0f,
                                endY = size.height
                            )
                        )
                    }
                },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            content()
        }
    }
@Preview(showBackground = true)
@Composable
fun BleScreenPreview() {
    TransDroidTheme {
        BleScreen()

    }
}