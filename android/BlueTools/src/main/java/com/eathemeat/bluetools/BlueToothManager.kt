package com.eathemeat.bluetools

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat.startActivityForResult


/**
 * author:PeterX
 * time:2024/5/5 0005
 */
@SuppressLint("MissingPermission")
object BlueToothManager {

    val TAG = "BlueToothManager"
    lateinit var appContext: Application

    val blueToothManager by lazy {
        if (isDevicesSupport()) {
            appContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        } else
            null
    }


    val  blueToothAdapter by lazy {
        blueToothManager?.adapter
    }

    val scanner by lazy {
        blueToothAdapter?.let {
            BlueToothScanner(appContext, it)
        }
    }

    fun init(appCtx:Application) {
        appContext = appCtx
    }


    fun isBluetoothEnable():Boolean {
        return blueToothAdapter.let {
            it?.isEnabled?:false
        }
    }

    fun isDevicesSupport(): Boolean {
        return true
    }

    /**
     * is support ble?
     *
     * @return
     */
    fun isSupportBle(): Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && appContext.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
    }


    fun enableBluetooth(enable:Boolean): Boolean {
        if (enable) {
            // For applications targeting Build.VERSION_CODES.TIRAMISU or above, this API will always fail and return false
            blueToothAdapter?.let { adapter->
                if (Build.VERSION_CODES.TIRAMISU > Build.VERSION.SDK_INT) {
                    return adapter.enable()
                } else {
                    if (!adapter.isEnabled) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        appContext.startActivity(enableBtIntent)
                    }
                }
            }
        }else {
            // For applications targeting Build.VERSION_CODES.TIRAMISU or above, this API will always fail and return false
            blueToothAdapter?.let { adapter->
                if (Build.VERSION_CODES.TIRAMISU > Build.VERSION.SDK_INT) {
                    return adapter.disable()
                } else {
                    if (adapter.isEnabled) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        appContext.startActivity(enableBtIntent)
                    }
                }
            }
        }
        return true
    }


    fun setDiscoverable(seconds:Int) {
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        appContext.startActivity(discoverableIntent)
    }

    fun cancelDiscovery():Unit {
        scanner?.cancelDiscovery()
    }

    fun startDiscovery(callBack: BlueToothScanner.CallBack):Unit {
        scanner?.startDiscovery(callBack)
    }












}