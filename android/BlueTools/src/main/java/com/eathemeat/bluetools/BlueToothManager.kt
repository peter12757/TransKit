package com.eathemeat.bluetools

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

/**
 * author:PeterX
 * time:2024/5/5 0005
 */
const val TAG = "BlueToothManager"

class BlueToothManager(val context:Context) {

    val blueToothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    var  blueToothAdapter: BluetoothAdapter?

    val receiver = object :BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onReceive(p0: Context, p1: Intent) {
            val action:String? = p1.action
            when(action) {
                BluetoothDevice.ACTION_FOUND ->{
                    val device: BluetoothDevice? = p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE,BluetoothDevice::class.java)
                    device?.let {
                        val devicesName = device.name
                        val deviceHAddress = device.address
                        val deviceAlias = device.alias
                    }


                }
            }
        }
    }

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