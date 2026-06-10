@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.example.flashshare.ui

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.draganddrop.dragAndDropDetector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// MARK: - Data Models

data class Device(
    val id: Long,
    val name: String,
    val type: DeviceType,
    val macAddress: String,
    val status: ConnectionStatus = ConnectionStatus.Unconnected
)

enum class DeviceType { Mobile, Laptop, Audio }
enum class ConnectionStatus { Connected, Unconnected }
enum class TransferStatus { Pending, Transferring, Completed, Error }
enum class ThemeMode { Dark, Light }
enum class ConnectionType { Bluetooth, WiFi }

data class FileItem(
    val id: Long = System.currentTimeMillis(),
    val uri: Uri? = null,
    val previewUrl: String,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String
) {
    val isImage: Boolean get() = mimeType.startsWith("image/")
}

data class TransferTask(
    val file: FileItem,
    var progress: Float = 0f,
    var status: TransferStatus = TransferStatus.Pending,
    var speed: Long = 0L // bytes per second
)

data class TransferUiState(
    val themeMode: ThemeMode = ThemeMode.Dark,
    val bluetoothEnabled: Boolean = true,
    val connectedDevice: Device? = null,
    val selectedFiles: List<FileItem> = emptyList(),
    val transferQueue: List<TransferTask> = emptyList(),
    val activeConnectionType: ConnectionType = ConnectionType.Bluetooth,
    val globalSpeed: Long = 0L,
    val globalEta: String = "--:--",
    val isScanning: Boolean = false,
    val showQrModal: Boolean = false,
    val toastMessage: ToastMessage? = null,
    val wifiHotspotEnabled: Boolean = false
)

data class ToastMessage(val text: String, val type: ToastType)

enum class ToastType { Info, Success, Error }

// MARK: - View Model

class FileTransferViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

    // Mock data
    private val mockDevices = listOf(
        Device(1, "iPhone 16 Pro", DeviceType.Mobile, "A4:C3:F1:22:90:11"),
        Device(2, "MacBook Air M4", DeviceType.Laptop, "B2:D4:E6:88:12:33"),
        Device(3, "Samsung Galaxy S25", DeviceType.Mobile, "C5:E7:F9:44:55:66"),
        Device(4, "Sony WH-1000XM6", DeviceType.Audio, "D6:F8:0A:66:77:88")
    )

    private val providedImageUrls = listOf(
        "https://agent.qianwen.com/mos/e9b2888b40c846f6892edee7805e5e4e/4b2a31eaf741c80fab75b60a28dd5ae2",
        "https://agent.qianwen.com/mos/e9b2888b40c846f6892edee7805e5e4e/0a0af13bb5186de47ed8dab9cb0bbdae",
        "https://agent.qianwen.com/mos/e9b2888b40c846f6892edee7805e5e4e/6a68a8682328f4c0f734e4746f8c9754",
        "https://agent.qianwen.com/mos/e9b2888b40c846f6892edee7805e5e4e/9c4684454978bcc71b7a68f057740107",
        "https://agent.qianwen.com/mos/e9b2888b40c846f6892edee7805e5e4e/c24fdc1ef8210a320a55af570963193f",
        "https://agent.qianwen.com/mos/e9b2888b40c846f6892edee7805e5e4e/cb6676401240ff3f4e784361fce85ae3",
        "https://agent.qianwen.com/mos/e9b2888b40c846f6892edee7805e5e4e/e3255a5a8ff9fa6cc2b576cbbe55e576"
    )

    init {
        // Pre-scan for demo
        scanDevices()
    }

    fun toggleTheme() {
        _uiState.value = _uiState.value.copy(
            themeMode = if (_uiState.value.themeMode == ThemeMode.Dark) ThemeMode.Light else ThemeMode.Dark
        )
    }

    fun toggleBluetooth() {
        _uiState.value = _uiState.value.copy(bluetoothEnabled = !_uiState.value.bluetoothEnabled)
        if (!_uiState.value.bluetoothEnabled) {
            showToast("蓝牙已关闭", ToastType.Info)
            _uiState.value = _uiState.value.copy(connectedDevice = null)
        } else {
            showToast("蓝牙已开启", ToastType.Info)
        }
    }

    fun switchConnectionType(type: ConnectionType) {
        _uiState.value = _uiState.value.copy(activeConnectionType = type)
    }

    fun scanDevices() {
        if (!uiState.value.bluetoothEnabled && uiState.value.activeConnectionType == ConnectionType.Bluetooth) {
            showToast("请先开启蓝牙", ToastType.Error)
            return
        }

        _uiState.value = _uiState.value.copy(isScanning = true)
        viewModelScope.launch {
            delay(1500)
            _uiState.value = _uiState.value.copy(isScanning = false)
            showToast("扫描完成，发现 ${mockDevices.size} 台设备", ToastType.Success)
        }
    }

    fun connectToDevice(device: Device) {
        val isConnected = uiState.value.connectedDevice?.id == device.id
        if (isConnected) {
            _uiState.value = _uiState.value.copy(connectedDevice = null)
            showToast("已断开与 ${device.name} 的连接", ToastType.Info)
        } else {
            _uiState.value = _uiState.value.copy(connectedDevice = device)
            showToast("成功连接到 ${device.name}", ToastType.Success)
        }
    }

    fun addSelectedFiles(files: List<FileItem>) {
        val updatedList = (uiState.value.selectedFiles + files).distinctBy { it.id }
        _uiState.value = _uiState.value.copy(selectedFiles = updatedList)
    }

    fun removeSelectedFile(file: FileItem) {
        _uiState.value = _uiState.value.copy(
            selectedFiles = uiState.value.selectedFiles.filter { it.id != file.id }
        )
    }

    fun clearAllSelectedFiles() {
        _uiState.value = _uiState.value.copy(selectedFiles = emptyList())
    }

    fun startTransfer() {
        val selected = uiState.value.selectedFiles
        val connected = uiState.value.connectedDevice

        when {
            selected.isEmpty() -> showToast("请先选择文件", ToastType.Error)
            connected == null -> showToast("请先连接设备", ToastType.Error)
            else -> {
                val newTasks = selected.map { file ->
                    TransferTask(file = file, status = TransferStatus.Pending)
                }
                _uiState.value = _uiState.value.copy(
                    transferQueue = uiState.value.transferQueue + newTasks,
                    selectedFiles = emptyList()
                )
                processNextTask()
            }
        }
    }

    private fun processNextTask() {
        val pendingTask = uiState.value.transferQueue.firstOrNull { it.status == TransferStatus.Pending || it.status == TransferStatus.Error }
        if (pendingTask == null) {
            _uiState.value = _uiState.value.copy(globalSpeed = 0L, globalEta = "--:--")
            showToast("所有文件传输完成", ToastType.Success)
            return
        }

        pendingTask.status = TransferStatus.Transferring
        _uiState.value = _uiState.value.copy()

        viewModelScope.launch {
            var progress = pendingTask.progress
            while (progress < 100f) {
                // Simulate network variation
                val increment = (kotlin.random.Random.nextFloat() * 5 + 1).coerceAtMost(100 - progress)
                progress += increment
                pendingTask.progress = progress.coerceAtMost(100f)

                // Simulate speed (2MB/s average)
                val speed = (kotlin.random.Random.nextLong(1024 * 1024, 3 * 1024 * 1024))
                _uiState.value = _uiState.value.copy(globalSpeed = speed)
                _uiState.value = _uiState.value.copy(globalEta = calculateETA(progress, pendingTask.file.fileSize))

                // Random error simulation
                if (progress > 20f && progress < 80f && kotlin.random.Random.nextDouble() > 0.995) {
                    pendingTask.status = TransferStatus.Error
                    _uiState.value = _uiState.value.copy()
                    showToast("传输失败: ${pendingTask.file.fileName}", ToastType.Error)
                    return@launch
                }

                _uiState.value = _uiState.value.copy()

                delay(100)
            }

            pendingTask.status = TransferStatus.Completed
            _uiState.value = _uiState.value.copy()
            processNextTask()
        }
    }

    fun retryTransfer(task: TransferTask) {
        task.status = TransferStatus.Pending
        task.progress = 0f
        _uiState.value = _uiState.value.copy()
        processNextTask()
    }

    fun toggleWifiHotspot() {
        _uiState.value = _uiState.value.copy(wifiHotspotEnabled = !uiState.value.wifiHotspotEnabled)
        showToast(
            if (uiState.value.wifiHotspotEnabled) "热点已开启" else "热点已关闭",
            ToastType.Info
        )
    }

    fun showQrCodeModal() {
        _uiState.value = _uiState.value.copy(showQrModal = true)
    }

    fun hideQrCodeModal() {
        _uiState.value = _uiState.value.copy(showQrModal = false)
    }

    private fun showToast(text: String, type: ToastType) {
        _uiState.value = _uiState.value.copy(toastMessage = ToastMessage(text, type))
        viewModelScope.launch {
            delay(3000)
            if (uiState.value.toastMessage?.text == text) {
                _uiState.value = _uiState.value.copy(toastMessage = null)
            }
        }
    }

    private fun calculateETA(currentPercent: Float, totalBytes: Long): String {
        if (currentPercent <= 0f) return "--:--"
        val remainingBytes = totalBytes * ((100 - currentPercent) / 100)
        val speed = uiState.value.globalSpeed.takeIf { it > 0 } ?: 2 * 1024 * 1024 // fallback
        val seconds = remainingBytes / speed.toDouble()
        val minutes = seconds / 60
        val m = minutes.toInt()
        val s = (seconds % 60).toInt()
        return "${m}:${s.toString().padStart(2, '0')}"
    }
}

// MARK: - UI Components

@Composable
fun FileTransferScreen(viewModel: FileTransferViewModel = remember { FileTransferViewModel() }) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLargeScreen = configuration.screenWidthDp >= 1024

    // Apply theme
    MaterialTheme(
        colorScheme = when (uiState.themeMode) {
            ThemeMode.Dark -> DarkColorScheme
            ThemeMode.Light -> LightColorScheme
        },
        typography = Typography,
        shapes = Shapes
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.radialGradient(if (uiState.themeMode == ThemeMode.Dark) CyberpunkDarkColors else CyberpunkLightColors))
                .padding(horizontal = if (isLargeScreen) 32.dp else 16.dp, vertical = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                HeaderSection(
                    isConnected = uiState.connectedDevice != null,
                    deviceName = uiState.connectedDevice?.name ?: "等待连接...",
                    onThemeToggle = viewModel::toggleTheme,
                    themeMode = uiState.themeMode
                )
                Spacer(modifier = Modifier.height(32.dp))
                MainContentArea(
                    uiState = uiState,
                    onScanClick = viewModel::scanDevices,
                    onConnectDevice = viewModel::connectToDevice,
                    onAddFiles = { /* Handled externally via ActivityResultLauncher */ },
                    onRemoveFile = viewModel::removeSelectedFile,
                    onClearAllFiles = viewModel::clearAllSelectedFiles,
                    onStartTransfer = viewModel::startTransfer,
                    onRetryTransfer = viewModel::retryTransfer,
                    onSwitchConnectionType = viewModel::switchConnectionType,
                    onShowQrCode = viewModel::showQrCodeModal,
                    onToggleHotspot = viewModel::toggleWifiHotspot,
                    isLargeScreen = isLargeScreen
                )
            }

            // Toast notifications
            ToastHost(
                hostState = remember { SnackbarHostState() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                uiState.toastMessage?.let { message ->
                    ToastMessageCard(message = message, onDismiss = { viewModel.hideToastMessage() })
                }
            }

            // QR Code Modal
            if (uiState.showQrModal) {
                QrCodeModal(onDismiss = viewModel::hideQrCodeModal)
            }
        }
    }
}

@Composable
private fun HeaderSection(
    isConnected: Boolean,
    deviceName: String,
    onThemeToggle: () -> Unit,
    themeMode: ThemeMode
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
}

@Composable
private fun MainContentArea(
    uiState: TransferUiState,
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
            .fillMaxSize()
            .weight(1f),
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
    uiState: TransferUiState,
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
                    selected = uiState.activeConnectionType == ConnectionType.Bluetooth,
                    onClick = { onSwitchConnectionType(ConnectionType.Bluetooth) }
                )
                TabButton(
                    text = "WiFi",
                    icon = painterResource(R.drawable.ic_wifi),
                    selected = uiState.activeConnectionType == ConnectionType.WiFi,
                    onClick = { onSwitchConnectionType(ConnectionType.WiFi) }
                )
            }

            AnimatedVisibility(visible = uiState.activeConnectionType == ConnectionType.Bluetooth) {
                BluetoothContent(
                    uiState = uiState,
                    onScanClick = onScanClick,
                    onConnectDevice = onConnectDevice
                )
            }

            AnimatedVisibility(visible = uiState.activeConnectionType == ConnectionType.WiFi) {
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
            .weight(1f)
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
private fun BluetoothContent(
    uiState: TransferUiState,
    onScanClick: () -> Unit,
    onConnectDevice: (Device) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("蓝牙开关", fontSize = 13.sp, color = Color.White)
            Switch(
                checked = uiState.bluetoothEnabled,
                onCheckedChange = { /* Handled in view model */ },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF00F3FF),
                    checkedTrackColor = Color(0xFF00F3FF).copy(alpha = 0.5f),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                )
            )
        }

        Box(modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val center = size.center
                val radius = size.minDimension / 2
                drawCircle(
                    brush = Brush.conicalGradient(
                        colors = listOf(Color.Transparent, Color(0x1A00F3FF), Color.Transparent),
                        center = centerOffset
                    ),
                    radius = radius,
                    center = center,
                    rotationDegrees = 0f // Will be animated
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_radar),
                contentDescription = "雷达扫描",
                tint = Color(0xFF00F3FF),
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }

        Button(
            onClick = onScanClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E90FF),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(painter = painterResource(R.drawable.ic_search), contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("搜索设备")
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.mockDevices) { device ->
                DeviceListItem(
                    device = device,
                    isConnected = uiState.connectedDevice?.id == device.id,
                    onClick = { onConnectDevice(device) }
                )
            }
        }
    }
}

@Composable
private fun WiFiContent(
    uiState: TransferUiState,
    onScanClick: () -> Unit,
    onConnectDevice: (Device) -> Unit,
    onShowQrCode: () -> Unit,
    onToggleHotspot: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gridCells(GridCells.Fixed(2))
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TactileButton(onClick = onScanClick, modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_wifi),
                        contentDescription = null,
                        tint = Color(0xFF0AFF60),
                        modifier = Modifier.size(24.dp)
                    )
                    Text("扫描网络", fontSize = 11.sp, color = Color.White)
                }
            }
            TactileButton(onClick = onShowQrCode, modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_qr_code),
                        contentDescription = null,
                        tint = Color(0xFFBC13FE),
                        modifier = Modifier.size(24.dp)
                    )
                    Text("扫码配对", fontSize = 11.sp, color = Color.White)
                }
            }
        }

        GlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("热点设置", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("开启热点", fontSize = 13.sp, color = Color.White)
                    Switch(checked = uiState.wifiHotspotEnabled, onCheckedChange = { onToggleHotspot() })
                }
                Text("SSID: FlashShare_Pro", fontSize = 11.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.mockDevices) { device ->
                DeviceListItem(
                    device = device,
                    isConnected = uiState.connectedDevice?.id == device.id,
                    onClick = { onConnectDevice(device) }
                )
            }
        }
    }
}

@Composable
private fun DeviceListItem(
    device: Device,
    isConnected: Boolean,
    onClick: () -> Unit
) {
    val iconRes = when (device.type) {
        DeviceType.Mobile -> R.drawable.ic_mobile
        DeviceType.Laptop -> R.drawable.ic_laptop
        DeviceType.Audio -> R.drawable.ic_headphones
    }

    val borderColor = if (isConnected) Color(0xFF00F3FF) else Color.White.copy(alpha = 0.1f)
    val bgColor = if (isConnected) Color(0xFF00F3FF).copy(alpha = 0.1f) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.1f), shape = CircleShape)
                    .padding(8.dp)
            ) {
                Icon(painter = painterResource(iconRes), contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Column {
                Text(device.name, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White)
                Text(device.macAddress, fontSize = 11.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
            }
        }
        Icon(
            painter = painterResource(
                if (isConnected) R.drawable.ic_link_connected else R.drawable.ic_arrow_right
            ),
            contentDescription = null,
            tint = if (isConnected) Color(0xFF0AFF60) else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun FileSelectionPanel(
    uiState: TransferUiState,
    onAddFiles: (List<FileItem>) -> Unit,
    onRemoveFile: (FileItem) -> Unit,
    onClearAllFiles: () -> Unit,
    onStartTransfer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDragging by remember { mutableStateOf(false) }

    GlassCard(
        modifier = modifier
            .fillMaxHeight()
            .dragAndDropDetector(
                onDragEnter = { isDragging = true },
                onDragExit = { isDragging = false },
                onDrop = { offset, data ->
                    isDragging = false
                    // Handle file drop (requires integration with ActivityResultLauncher)
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val showPreview = uiState.selectedFiles.isNotEmpty()

            AnimatedVisibility(visible = !showPreview) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .border(
                            width = 2.dp,
                            brush = Brush.dashed(
                                color = if (isDragging) Color(0xFF00F3FF) else Color.White.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                brush = Brush.horizontalGradient(listOf(Color(0x2000F3FF), Color(0x20BC13FE))),
                                shape = CircleShape
                            )
                            .padding(16.dp)
                            .animateEnterExit(enter = fadeIn() + scaleIn(), exit = fadeOut()),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cloud_upload),
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("拖拽文件到这里", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("支持图片、视频、文档等任意格式", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    TactileButton(onClick = { /* Launch file picker */ }) {
                        Text("或点击选择文件", fontSize = 13.sp)
                    }
                }
            }

            AnimatedVisibility(visible = showPreview) {
                Column(modifier = Modifier.fillMaxSize().weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "已选文件 (${uiState.selectedFiles.size})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        TextButton(onClick = onClearAllFiles) {
                            Text("清空列表", fontSize = 12.sp, color = Color(0xFFFF6347))
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 80.dp),
                        modifier = Modifier.weight(1f).padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.selectedFiles) { file ->
                            FilePreviewItem(file = file, onRemove = { onRemoveFile(file) })
                        }
                    }

                    Button(
                        onClick = onStartTransfer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBC13FE),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("立即发送", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(painter = painterResource(R.drawable.ic_send), contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FilePreviewItem(
    file: FileItem,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp))
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = file.previewUrl),
            contentDescription = file.fileName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.8f)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
                .padding(8.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Text(file.fileName, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                Text(formatFileSize(file.fileSize), fontSize = 9.sp, color = Color.LightGray)
            }
        }
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .background(Color.Red.copy(alpha = 0.8f), shape = CircleShape)
        ) {
            Icon(Icons.Default.Close, contentDescription = "删除", tint = Color.White, modifier = Modifier.size(12.dp))
        }
    }
}

@Composable
private fun TransferQueuePanel(
    uiState: TransferUiState,
    onRetryTransfer: (TransferTask) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier.fillMaxHeight()) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text("传输队列", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)

            // Global Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatBox(label = "实时速度", value = "${formatFileSize(uiState.globalSpeed)}/s", color = Color(0xFF00F3FF))
                StatBox(label = "剩余时间", value = uiState.globalEta, color = Color.White)
            }

            // Queue List
            if (uiState.transferQueue.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.ic_folder_open),
                            contentDescription = null,
                            tint = Color.Gray.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                        Text("暂无传输任务", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.transferQueue) { task ->
                        TransferQueueItem(task = task, onRetry = { onRetryTransfer(task) })
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, color: Color) {
    Box(
        modifier = Modifier
            .weight(1f)
            .background(Color.Black.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun TransferQueueItem(
    task: TransferTask,
    onRetry: () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(model = task.file.previewUrl),
                    contentDescription = task.file.fileName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp))
                )
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(task.file.fileName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                        StatusIcon(status = task.status)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${formatFileSize((task.file.fileSize * task.progress / 100).toLong())} / ${formatFileSize(task.file.fileSize)}", fontSize = 11.sp, color = Color.Gray)
                        Text("${task.progress.toInt()}%", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = { task.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(vertical = 8.dp)
                    .drawBehind {
                        drawRect(
                            brush = Brush.horizontalGradient(listOf(Color(0xFF00F3FF), Color(0xFFBC13FE))),
                            size = Size(size.width, size.height)
                        )
                    },
                trackColor = Color.Black.copy(alpha = 0.2f),
                indicatorColor = Color.Transparent
            )

            // Retry button only for failed tasks
            if (task.status == TransferStatus.Error) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onRetry) {
                        Text("重试", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusIcon(status: TransferStatus) {
    val (icon, color) = when (status) {
        TransferStatus.Completed -> Pair(painterResource(R.drawable.ic_check_circle), Color(0xFF0AFF60))
        TransferStatus.Error -> Pair(painterResource(R.drawable.ic_error), Color(0xFFFF6347))
        TransferStatus.Transferring -> Pair(painterResource(R.drawable.ic_spinner), Color(0xFF00F3FF))
        TransferStatus.Pending -> Pair(painterResource(R.drawable.ic_clock), Color.Gray)
    }
    Icon(painter = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
}

@Composable
private fun TactileButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    Button(
        onClick = onClick,
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        scale = 0.95f
                        tryAwaitRelease()
                        scale = 1f
                    }
                )
            },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(12.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.labelMedium.copy(color = Color.White)) {
            content()
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

@Composable
private fun Modifier.glassSurface(): Modifier = this
    .background(
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.05f),
                Color.Transparent
            )
        )
    )
    .then(
        if (BuildConfig.DEBUG) Modifier.border(1.dp, Color.Red) else Modifier
    )
    .shadow(elevation = 8.dp, shape = this.layoutDirection)

@Composable
private fun QrCodeModal(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {},
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("扫码连接设备", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("请使用另一台设备扫描下方二维码", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))
                Image(
                    painter = painterResource(R.drawable.qr_code_placeholder),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(192.dp)
                        .padding(16.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(4.dp)
                )
                Text("642 918", fontSize = 12.sp, color = Color.Black, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss) {
                    Text("关闭")
                }
            }
        }
    )
}

@Composable
private fun ToastMessageCard(message: ToastMessage, onDismiss: () -> Unit) {
    val backgroundColor = when (message.type) {
        ToastType.Success -> Color(0xFF0AFF60).copy(alpha = 0.2f)
        ToastType.Error -> Color(0xFFFF6347).copy(alpha = 0.2f)
        ToastType.Info -> Color.White.copy(alpha = 0.1f)
    }
    val borderColor = when (message.type) {
        ToastType.Success -> Color(0xFF0AFF60).copy(alpha = 0.5f)
        ToastType.Error -> Color(0xFFFF6347).copy(alpha = 0.5f)
        ToastType.Info -> Color.White.copy(alpha = 0.2f)
    }
    val textColor = when (message.type) {
        ToastType.Success -> Color(0xFF0AFF60)
        ToastType.Error -> Color(0xFFFF6347)
        ToastType.Info -> Color.White
    }

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .animateEnterExit(
                enter = slideInHorizontally { it } + fadeIn(),
                exit = slideOutHorizontally { it } + fadeOut()
            ),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(
                    when (message.type) {
                        ToastType.Success -> R.drawable.ic_check_circle
                        ToastType.Error -> R.drawable.ic_error
                        ToastType.Info -> R.drawable.ic_info
                    }
                ),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
            Text(message.text, color = textColor, fontSize = 13.sp)
        }
    }
}

// MARK: - Utilities

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${String.format("%.1f", bytes / 1024f)} KB"
        bytes < 1024 * 1024 * 1024 -> "${String.format("%.1f", bytes / 1024f / 1024f)} MB"
        else -> "${String.format("%.1f", bytes / 1024f / 1024f / 1024f)} GB"
    }
}

// MARK: - Design System

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00F3FF),
    secondary = Color(0xFFBC13FE),
    background = Color(0xFF0F0F1A),
    surface = Color(0xFF1A1A2E),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00F3FF),
    secondary = Color(0xFFBC13FE),
    background = Color(0xFFF5F7FA),
    surface = Color(0xFFC3CFE2),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val Typography = Typography(
    bodyLarge = androidx.compose.material3.MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.SansSerif),
    titleLarge = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.SansSerif)
)

private val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

private val CyberpunkDarkColors = listOf(
    Color(0xFF1A1A2E),
    Color(0xFF16213E),
    Color(0xFF0F0F1A)
)

private val CyberpunkLightColors = listOf(
    Color(0xFFF5F7FA),
    Color(0xFFC3CFE2)
)