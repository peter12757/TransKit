package com.eathemeat.transkit.bluetooth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.eathemeat.bluetools.RemoteDevice

/**
 * author:PeterX
 * time:2024/5/15 0015
 */
class BluetoothViewModel:ViewModel() {

    companion object {
        enum class DeviceState(name:String) {
            Idle("Idle"),
            Connecting("Connecting"),
            Connected("Connected"),
            Error("Error"),

        }
    }

    val devices = mutableListOf<RemoteDevice>()

    val currDevice = mutableStateOf<RemoteDevice>(RemoteDevice())

    val deviceState = mutableStateOf(DeviceState.Idle)

    enum class ScreenState{
        Discovery,
        BLE,
        AUDIO,

    }

    var screenState = mutableStateOf(ScreenState.Discovery)



    fun test() {
     }
}