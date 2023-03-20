package com.eathemeat.trans

import android.app.Application
import com.eathemeat.trans.data.SampleItem
import com.eathemeat.trans.data.SampleState

class MainApplication : Application() {

    val sampleItems = mutableListOf<SampleItem>()

    override fun onCreate() {
        super.onCreate()
        initData()
    }

    private fun initData() {
        sampleItems.add(SampleItem("蓝牙传输",0,"蓝牙传输功能", SampleState.DOING))
        sampleItems.add(SampleItem("红外传输",0,"红外传输功能", SampleState.TODO))
        sampleItems.add(SampleItem("socket传输",0,"socket传输功能", SampleState.TODO))
        sampleItems.add(SampleItem("局域网传输",0,"局域网传输功能", SampleState.TODO))
    }


}