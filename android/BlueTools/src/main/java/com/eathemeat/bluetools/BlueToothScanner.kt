package com.eathemeat.bluetools

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


/**
 * author:PeterX
 * time:2024/5/5 0005
 */
@SuppressLint("MissingPermission")
class BlueToothScanner(val context:Context,val adapter:BluetoothAdapter) {

    val TAG = "BlueToothScanner"

        val receiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onReceive(p0: Context, p1: Intent) {
            val action:String? = p1.action
            when(action) {
                BluetoothDevice.ACTION_FOUND ->{
                    val device: BluetoothDevice? = p1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE,BluetoothDevice::class.java)
                    device?.let {
//                        val devicesName = device.name
//                        val deviceHAddress = device.address
//                        val deviceAlias = device.alias
                        Log.d(TAG, "onReceive: ${device}")
                        mCallBack?.let {
                            it.onDevice(device)
                        }
                    }
                }
            }
        }
    }




    var mCallBack:CallBack? = null

    fun startDiscovery(callBack: CallBack):Unit {
        if (mCallBack != null) throw Exception("only start once at one time")
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
        mCallBack = callBack
        mCallBack?.let {
            adapter.bondedDevices.forEach{device->
                it.onDevice(device)
            }
            adapter.startDiscovery()
        }

    }

    fun cancelDiscovery():Unit {
        context.unregisterReceiver(receiver)
        adapter.cancelDiscovery()
    }


    interface CallBack {

        fun onDevice(device:BluetoothDevice)
    }









}