package com.eathemeat.transdroid.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.main.ui.IState
import com.eathemeat.transdroid.main.ui.StateManager
import com.eathemeat.transdroid.main.ui.screen.launcher.LauncherScreen
import com.eathemeat.transdroid.main.ui.screen.launcher.LauncherState
import kotlinx.coroutines.flow.MutableStateFlow

class MainModel: ViewModel() {


    val stateManager = StateManager()
    val curState = MutableStateFlow(stateManager.curState)


    init {
        stateManager.put(LauncherState(), { LauncherScreen() })
        Logger.d()
        stateManager.transfer2State(LauncherState.tag())
    }

    fun screen(state: IState): @Composable (() -> Unit)? {
        return stateManager.get(state)
    }


}