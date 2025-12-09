package com.eathemeat.transdroid.main.ui.screen.launcher

import com.eathemeat.transdroid.main.ui.screen.IState
import com.eathemeat.transdroid.main.util.ChoreHandler

class LauncherState : IState {

    private val def_ShoweTime = 1000*5

    var showTime = def_ShoweTime

    val showRunnable = Runnable {
        if (showTime >0) {
            showTime-= 1000
            startTime()
        } else {
            showTime = def_ShoweTime
        }
    }


    fun startTime(){
        if (!ChoreHandler.hasCallbacks(showRunnable)) {
            ChoreHandler.postDelayed(showRunnable,1000)
        }
    }



}