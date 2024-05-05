package com.eathemeat.bluetools

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log

/**
 * author:PeterX
 * time:2024/5/5 0005
 */
const val TAG = "BlueToothManager"

class BlueToothManager(val context:Context) {

    val blueToothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    var  blueToothAdapter: BluetoothAdapter?

    init {
        blueToothAdapter = blueToothManager.adapter
        if (blueToothAdapter == null) {
            Log.e(TAG, "Device doesn't support Bluetooth: ", Throwable())
        }
    }


    fun isDevicesSupport(): Boolean {
        return blueToothAdapter == null
    }








}