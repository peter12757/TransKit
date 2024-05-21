package com.eathemeat.bluetools

import androidx.bluetooth.BluetoothDevice
import java.util.UUID

/**
 * author:PeterX
 * time:2024/5/15 0015
 */
class RemoteDevice(val type:Type = Type.TYPE_UNKONWN,val name: String = "unknown") {

    enum class Type{
        TYPE_UNKONWN,
        TYPE_BLUETOOTH,
        TYPE_BLE,
        TYPE_BLEAUDIO
    }


    val bluetoothDevice:BluetoothDevice? = null

    fun name(): String {
        return when(type) {
            Type.TYPE_BLUETOOTH -> bluetoothDevice!!.name?:let { "null" }
            Type.TYPE_UNKONWN -> "unknown"
        }
    }

    fun id(): String {
        return when(type) {
            Type.TYPE_BLUETOOTH -> bluetoothDevice!!.id?.toString()!!
            Type.TYPE_UNKONWN -> "unknown"
        }
    }
}

