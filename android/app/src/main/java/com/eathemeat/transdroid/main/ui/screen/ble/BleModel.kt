package com.eathemeat.transdroid.main.ui.screen.ble

import com.eathemeat.transdroid.main.data.ConnectionType
import com.eathemeat.transdroid.main.data.Device
import com.eathemeat.transdroid.main.data.FileItem
import com.eathemeat.transdroid.main.data.TransferTask

class BleModel {

    var connectedDevice: Device?
        get() {
            return connectedDevice
        }
        set(value) {}

    val selectedFiles: List<FileItem> = emptyList()
//    val transferQueue: List<TransferTask> = emptyList(),
val globalSpeed: Long = 0L
    val globalEta: String = "--:--"
    val isScanning: Boolean = false
    val showQrModal: Boolean = false



    fun scanDevices() {

    }

    fun connectToDevice(dev:Device) {

    }

    fun onAddFiles(files:List<FileItem>) {

    }


    fun onRemoveFile(file:FileItem) {

    }


    fun onClearAllFiles() {

    }

    fun onStartTransfer() {

    }

    fun onRetryTransfer(task:TransferTask) {

    }

    fun switchConnectionType(type:ConnectionType) {

    }

    fun showQrCodeModal() {

    }

    fun toggleWifiHotspot() {

    }
}