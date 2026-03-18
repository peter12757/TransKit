package com.eathemeat.transdroid.main.ui.screen.home

import android.os.Message
import android.util.Log
import com.eathemeat.base.util.Logger

class HomeSate: IState {

    companion object {
        fun tag():String {
            return this::class.java.name?:"HomeSate"
        }
    }

    override fun TAG(): String {
        return Companion.tag()
    }

    override fun enter() {
        Logger.d()
    }

    override fun exit() {
        Logger.d()
    }

    override fun handleMsg(msg: Message) {
        Log.d(TAG(), "handleMsg: ")
    }
}