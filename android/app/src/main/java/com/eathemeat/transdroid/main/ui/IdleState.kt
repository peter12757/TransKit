package com.eathemeat.transdroid.main.ui

import com.eathemeat.base.util.Logger

class IdleState : IState{

    val TAG = "IdleState"
    override fun TAG(): String {
        return "IdleState"
    }

    override fun enter() {
        Logger.d(TAG,"enter")
    }

    override fun exit() {
        Logger.d(TAG,"enter")
    }
}