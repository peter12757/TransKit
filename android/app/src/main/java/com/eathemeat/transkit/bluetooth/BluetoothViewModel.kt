package com.eathemeat.transkit.bluetooth

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import com.eathemeat.bluetools.BluetoothDeviceWrapper

/**
 * author:PeterX
 * time:2024/5/15 0015
 */
class BluetoothViewModel:ViewModel() {

    val devices = mutableListOf<BluetoothDeviceWrapper>()
}