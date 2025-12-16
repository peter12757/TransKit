package com.eathemeat.transdroid.main.util

import android.os.Build
import android.os.Handler
import android.os.HandlerThread

object ChoreThread:HandlerThread("ChoreHandleThread") {
    fun hasCallbacks(runnable: Runnable): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             return handler.hasCallbacks(runnable)
        }
        return false    //has problem
    }

    fun postDelayed(runnable: Runnable, delay: Long) {
        handler.postDelayed(runnable,delay)
    }


    lateinit var handler: Handler

    init {
        start()
        handler = Handler(ChoreThread.looper)
    }


}