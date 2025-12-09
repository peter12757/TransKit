package com.eathemeat.transdroid.main.util

import android.os.Handler
import android.os.HandlerThread

object ChoreHandleThread:HandlerThread("ChoreHandleThread")

object ChoreHandler : Handler(ChoreHandleThread.looper) {

    init {
        ChoreHandleThread.start()
    }





}