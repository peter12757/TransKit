package com.eathemeat.transkit.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlin.reflect.KClass

/**
 * author:PeterX
 * time:2024/4/14 0014
 */
// TODO:  
open class TransKitItem(val name:String = "unknow", val desc:String="",var target:Any,val pretreatment:Intent.()->Unit) {

    inline fun transferto(ctx:Context): Unit {
        if (target is Class<*>) {
            var intent = Intent(ctx, target as Class<*>)
            intent.pretreatment()
            ctx.startActivity(intent)
        } else {
            throw Exception("target is Class<*> is false")
        }
    }
}

