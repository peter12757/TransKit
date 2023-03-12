package com.eathemeat.ble

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.eathemeat.basedroid.Const
import com.eathemeat.ble.api.OnScanCallback
import com.eathemeat.ble.data.BleConfig
import java.util.*


var TAG = BleManager::class.java.simpleName
class BleManager() {

    companion object {
        val sIntance = BleManager()
    }

    lateinit var app:Application
    private var bluetoothManager: BluetoothManager? = null
    private var bleConfig: BleConfig = BleConfig()
    private var bluetoothAdapter: BluetoothAdapter? = null

    fun init(app: Application): Unit {
        if (this.app != null) {
            throw ExceptionInInitializerError("already inited")
        }
        this.app = app
            bluetoothManager =
                app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager!!.adapter
    }


    /**
     * is support ble?
     *
     * @return
     */
    fun isSupportBle(): Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && app.getApplicationContext().getPackageManager()
            .hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
    }


    fun scan(callback:OnScanCallback) :Unit {
        synchronized(this){
            if (callback == null) {
                throw IllegalArgumentException("BleScanCallback can not be Null!");
            }
            if (!isBlueEnable()) {
                Log.e(TAG,"Bluetooth not enable!")
                callback.onError(Const.ErrorCode.BLUE_UNABLE.value)
                return
            }

            val serviceUuids: Array<UUID>? = bleConfig.mServiceUuids
            val deviceNames: Array<String>? = bleConfig.mDeviceNames
            val deviceMac: String? = bleConfig.mDeviceMac
            val fuzzy: Boolean? = bleConfig.mFuzzy
            val timeOut: Long? = bleConfig.mScanTimeOut
            var mBlueState = Const.BlueState.STATE_IDLE;

            if (mBlueState !== Const.BlueState.STATE_IDLE) {
                Log.w(TAG,"scan action already exists, complete the previous scan action first")
                if (callback != null) {
                    callback.onScanComplete(mutableListOf())    // TODO: 已经在扫描了，是否需要调用onerroe
                }
                return
            }

            var scanner:BluetoothLeScanner = bluetoothAdapter!!.bluetoothLeScanner
            if (ActivityCompat.checkSelfPermission(
                    app,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            )  
                scanner.startScan(object : ScanCallback(){

                    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                        super.onBatchScanResults(results)
                    }

                    override fun onScanFailed(errorCode: Int) {
                        super.onScanFailed(errorCode)
                    }

                    override fun onScanResult(callbackType: Int, result: ScanResult?) {
                        super.onScanResult(callbackType, result)
                    }
            })
            mBlueState = Const.BlueState.STATE_SCANNING

        }

    }

    fun connect() :Unit {

    }

    fun  disconnect():Unit {

    }



    /**
     * stopScan
     */
    fun stopScan(): Unit {

    }

    /**
     * judge Bluetooth is enable
     *
     * @return
     */
    fun isBlueEnable(): Boolean {
        return bluetoothAdapter != null && bluetoothAdapter!!.isEnabled
    }
}