package com.eathemeat.transkit.main

import android.app.Activity
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.eathemeat.transkit.bluetooth.BluetoothActivity

/**
 * author:PeterX
 * time:2024/4/14 0014
 */
class MainViewModel: ViewModel() {

    private var _transKitList = mutableListOf<TransKitItem>()

    val transKitList:List<TransKitItem>
        get() = _transKitList


    init {
        _transKitList.add(TransKitItem("蓝牙传输(TraditionBluetooth)","使用传统蓝牙进行传输",BluetoothActivity::class.java){

        })
    }



    fun asd() {
    }
}

