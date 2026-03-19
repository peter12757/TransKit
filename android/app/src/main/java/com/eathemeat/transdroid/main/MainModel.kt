package com.eathemeat.transdroid.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.main.ui.screen.home.HomeSate
import com.eathemeat.transdroid.main.ui.screen.launcher.LauncherScreen
import com.eathemeat.transdroid.main.ui.screen.launcher.LauncherState
import kotlinx.coroutines.flow.MutableStateFlow

class MainModel: ViewModel() {



    init {
        Logger.d()
    }

    val launcherState = LauncherState()
    val homeState = HomeSate()


}