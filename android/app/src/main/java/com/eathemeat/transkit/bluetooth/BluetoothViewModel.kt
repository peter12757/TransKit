package com.eathemeat.transkit.bluetooth

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.eathemeat.bluetools.BlueToothManager
import com.eathemeat.bluetools.BlueToothScanner
import com.eathemeat.bluetools.RemoteDevice

/**
 * author:PeterX
 * time:2024/5/15 0015+41
 *
 *
 */
class BluetoothViewModel(app:Application): AndroidViewModel(app) {
    \


        companion object {
        enum class DeviceState(name:String) {
            Idle("Idle"),
            Connecting("Connecting"),
            Connected("Connected"),
            Error("Error"),
        }
    }

    val TAG = BluetoothViewModel::class.java.name


    val devices = mutableListOf<RemoteDevice>()

    val currDevice = mutableStateOf<RemoteDevice>(RemoteDevice())

    val deviceState = mutableStateOf(DeviceState.Idle)
    val bluetoothManager = BlueToothManager
    val enableBluetooth = mutableStateOf(bluetoothManager.isBluetoothEnable())

    enum class ScreenState{
        Discovery,
        BLE,
        AUDIO,
    }

    init {

    }

    var screenState = mutableStateOf(ScreenState.Discovery)



    fun test() {
    }

    fun enableBluetooth(enableBluetooth: Boolean):Boolean {
        return bluetoothManager.enableBluetooth(enableBluetooth)
    }

    fun startDiscovery() {
        bluetoothManager.startDiscovery(callBack = object:BlueToothScanner.CallBack {
            override fun onDevice(device: BluetoothDevice) {
                Log.d(TAG, "onDevice: ${device}")
            }

        })
    }
}