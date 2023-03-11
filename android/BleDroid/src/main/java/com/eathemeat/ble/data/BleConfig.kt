package com.eathemeat.ble.data

import com.eathemeat.ble.BleManager
import java.util.*


class BleConfig() {

    val DEFAULT_SCAN_TIME:Long = 10000
     val DEFAULT_MAX_MULTIPLE_DEVICE = 7
     val DEFAULT_OPERATE_TIME = 5000
     val DEFAULT_CONNECT_RETRY_COUNT = 0
     val DEFAULT_CONNECT_RETRY_INTERVAL = 5000
     val DEFAULT_MTU = 23
     val DEFAULT_MAX_MTU = 512
     val DEFAULT_WRITE_DATA_SPLIT_COUNT = 20
     val DEFAULT_CONNECT_OVER_TIME = 10000

    var maxConnectCount = DEFAULT_MAX_MULTIPLE_DEVICE
    var operateTimeout = DEFAULT_OPERATE_TIME
    var reConnectCount = DEFAULT_CONNECT_RETRY_COUNT
    var reConnectInterval = DEFAULT_CONNECT_RETRY_INTERVAL.toLong()
    var splitWriteNum = DEFAULT_WRITE_DATA_SPLIT_COUNT
    var connectOverTime = DEFAULT_CONNECT_OVER_TIME.toLong()

    var mServiceUuids: Array<UUID>? = null
    var mDeviceNames: Array<String>? = null
    var mDeviceMac: String? = null
    var mAutoConnect = false
    var mFuzzy = false
    var mScanTimeOut: Long = DEFAULT_SCAN_TIME




}