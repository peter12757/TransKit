package com.eathemeat.bluetools

import androidx.bluetooth.BluetoothDevice

/**
 * author:PeterX
 * time:2024/5/15 0015
 */
class BluetoothDeviceWrapper(val type:Type = Type.TYPE_BLUETOOTH_DEVICE) {

    enum class Type{
        TYPE_BLUETOOTH_DEVICE,
        TYPE_BLUETOOTH_DEVICE_TEST
    }

    val device:BluetoothDevice? = null

    val deviceTest:BluetoothDeviceTest?= null



    data class BluetoothDeviceTest(val name: String = "asda", val address: String = "asdhajshd")
}

