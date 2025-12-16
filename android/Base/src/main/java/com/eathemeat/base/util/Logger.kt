package com.eathemeat.base.util

import android.util.Log

object Logger {


    fun d(msg:String="") {
        val stack = Thread.currentThread().stackTrace
        var isSelf = false
        for (element in stack) {
            val name =getSimpleClassName(element.className)
            if (isSelf) {
                Log.e("${name}","${name}::${element.methodName}::${element.lineNumber} $msg")
                break
            }else if (name == this::class.simpleName) {
//                Log.e("${name}","${name}::${element.methodName}::${element.lineNumber}"+"111111")
                isSelf = true
            }else {
//                Log.e("${name}","${name}::${element.methodName}::${element.lineNumber}"+"222222")
            }
        }
    }

    fun getSimpleClassName(classname:String): String {
        return classname.split(".").last()
    }

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