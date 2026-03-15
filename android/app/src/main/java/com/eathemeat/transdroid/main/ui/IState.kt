package com.eathemeat.transdroid.main.ui

import android.os.Message

interface IState {

    fun TAG():String


    fun enter()
    fun exit()

    fun handleMsg(msg: Message)
}