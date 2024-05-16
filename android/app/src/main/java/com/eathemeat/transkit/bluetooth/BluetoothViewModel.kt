package com.eathemeat.transkit.bluetooth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.eathemeat.bluetools.RemoteDevice

/**
 * author:PeterX
 * time:2024/5/15 0015
 */
class BluetoothViewModel:ViewModel() {

    val devices = mutableListOf<RemoteDevice>()

    enum class ScreenState{
        Discovery,
        BLE,
        AUDIO,

    }

    var screenState = mutableStateOf(ScreenState.Discovery)



    fun test() {
     }
}