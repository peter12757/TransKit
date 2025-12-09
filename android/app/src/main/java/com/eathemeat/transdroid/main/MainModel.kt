package com.eathemeat.transdroid.main

import androidx.lifecycle.ViewModel
import com.eathemeat.transdroid.main.ui.screen.IState
import com.eathemeat.transdroid.main.ui.screen.launcher.LauncherState
import kotlinx.coroutines.flow.MutableStateFlow

class MainModel: ViewModel() {

    private val launcherState = LauncherState()
    private val curState: IState = launcherState

    val launcherTime = MutableStateFlow(launcherState.showTime)



}