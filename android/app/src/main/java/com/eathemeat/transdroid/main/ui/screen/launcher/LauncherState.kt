package com.eathemeat.transdroid.main.ui.screen.launcher

import android.annotation.SuppressLint
import android.os.Message
import com.eathemeat.transdroid.main.util.ChoreThread
import kotlinx.coroutines.flow.MutableStateFlow

class LauncherState  {

    companion object {
        fun tag(): String {
            return "LauncherState"
        }
    }

    private val def_ShoweTime = 1000*5


    var showTime = def_ShoweTime

    val launcherTime = MutableStateFlow(showTime)

    val showRunnable = Runnable {
        if (showTime >0) {
            showTime-= 1000
            launcherTime.value = showTime
            startTime()
        } else {
            showTime = def_ShoweTime
        }
    }


    fun startTime(){
        if (!ChoreThread.hasCallbacks(showRunnable)) {
            ChoreThread.postDelayed(showRunnable,1000)
        }
    }


    fun enter() {
        launcherTime.value = showTime
        startTime()
    }

    fun exit() {
        showTime = def_ShoweTime
    }

    fun handleMsg(msg: Message) {

    }


}