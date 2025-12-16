package com.eathemeat.ble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.eathemeat.base.util.Logger
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat


class BleTrans(val ctx: Context) {

    private  val TAG = "BleTrans"



    val bluetoothManager: BluetoothManager
    val mBluetoothAdapter: BluetoothAdapter

    init {
        bluetoothManager = ctx.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter= bluetoothManager.adapter
    }


    /**
     * 请求位置权限
     *
     * @param activity 上下文
     * @return boolean
     */
    fun isGrantLocationPermission(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity.requestPermissions(
                arrayOf<String?>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    ), 1
            )

            return false
        }
        return true
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun enableBluetooth() {
        if (!mBluetoothAdapter.isEnabled || checkBluePermisssion()) {
            var enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            ctx.startActivity(enableBtIntent)
        }
    }

    fun checkBluePermisssion(): Boolean {
        return ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
    }


//    private var mDevice: BluetoothDevice? = null
//    val mLeScanCallback: ScanCallback = LeScanCallback @androidx.annotation.RequiresPermission(
//        android.Manifest.permission.BLUETOOTH_CONNECT
//    ) { device, rssi, scanRecord ->
//        Log.d(TAG, "onLeScan:  " + device.name + " : " + rssi)
//        if (device.name != null) {
//            deviceName.setText(device.name)
//            if (device.name == "test_ble") { // 或者比较 mac 地址
//                mDevice = device
//                mBluetoothAdapter.stopLeScan(mLeScanCallback)
//            }
//        }
//    }

    val mScanCallBack: ScanCallback = object:ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            Logger.d(TAG,"errorCode$errorCode")

        }

        override fun onBatchScanResults(results: List<ScanResult?>?) {
            Logger.d(TAG,"results${results}")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Logger.d(TAG,"callbackType:$callbackType, result:${result}")
            when (result) {
                null -> {

                }
                else ->{

                }
            }
        }
    }


}