package com.eathemeat.transdroid.main.ui.screen.home

import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.main.ui.IState

class HomeSate: IState {

    companion object {
        fun tag():String {
            return this::class.java.name?:"HomeSate"
        }
    }

    override fun tag(): String {
        return Companion.tag()
    }

    override fun enter() {
        Logger.d()
    }

    override fun exit() {
        Logger.d()
    }
}