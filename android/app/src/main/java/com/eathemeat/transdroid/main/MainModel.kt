package com.eathemeat.transdroid.main

import androidx.lifecycle.ViewModel
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.main.ui.screen.home.HomeSate
import com.eathemeat.transdroid.main.ui.screen.advertisement.AdState

class MainModel: ViewModel() {



    init {
        Logger.d()
    }

    val launcherState = AdState()
    val homeState = HomeSate()


}