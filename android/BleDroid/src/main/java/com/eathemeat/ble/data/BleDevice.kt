package com.eathemeat.ble.data

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import android.os.Parcelable

class BleDevice() :Parcelable {

    private lateinit var mDevice: BluetoothDevice
    private lateinit var mScanRecord: ByteArray
    private var mRssi = 0
    private var mTimestampNanos: Long = 0


    constructor(device: BluetoothDevice):this(){
        mDevice = device
    }

    constructor(device: BluetoothDevice, rssi:Int, scanRecord:ByteArray, timestampNanos:Long):this(){
        mDevice = device
        mScanRecord = scanRecord
        mRssi = rssi
        mTimestampNanos = timestampNanos
    }

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BleDevice> {
        override fun createFromParcel(parcel: Parcel): BleDevice {
            return BleDevice(parcel)
        }

        override fun newArray(size: Int): Array<BleDevice?> {
            return arrayOfNulls(size)
        }
    }


}