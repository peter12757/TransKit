package com.eathemeat.ble.api

import com.eathemeat.ble.data.BleDevice

interface OnScanCallback {

    /**
     * 错误扫描过程中的中断等
     */
    fun onError(code: Int)

    /**
     * 扫描过程中返回设备
     */
    fun onScan(devices: List<BleDevice>?): Unit

    /**
     *扫描完成，返回设备
     */
    fun onScanComplete(devices:List<BleDevice>):Unit

}