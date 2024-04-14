package com.eathemeat.transkit.ui.main

import androidx.lifecycle.ViewModel

/**
 * author:PeterX
 * time:2024/4/14 0014
 */
class MainViewModel: ViewModel() {

    private var _transKitList = mutableListOf<TransKitItem>()

    val transKitList:List<TransKitItem>
        get() = _transKitList


    init {
        _transKitList.add(TransKitItem("蓝牙传输","使用蓝牙进行传输"))
    }
}