package com.eathemeat.base.util

import android.util.Log

object Logger {



    fun d(tag:String,msg:String,throwable: Throwable? = null) {
        Log.d(tag,msg,throwable)
    }

    fun i(tag:String,msg:String,throwable: Throwable? = null) {
        Log.i(tag,msg,throwable)
    }

    fun e(tag:String,msg:String,throwable: Throwable? = null) {
        Log.e(tag,msg,throwable)
    }

    fun w(tag:String,msg:String,throwable: Throwable? = null) {
        Log.w(tag,msg,throwable)
    }

    fun v(tag:String,msg:String,throwable: Throwable? = null) {
        Log.v(tag,msg,throwable)
    }

}