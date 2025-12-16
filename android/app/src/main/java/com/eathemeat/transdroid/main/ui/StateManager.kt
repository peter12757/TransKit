package com.eathemeat.transdroid.main.ui

import androidx.compose.runtime.Composable

open class StateManager : HashMap<IState, @Composable (() -> Unit)>() {

    var curState: IState = IdleState()

    val tagMap = mutableMapOf<String, IState>()


    fun transfer2State(state: IState) { //shoule be in a thread?
        synchronized(this) {
            curState.exit()
            curState = state
            curState.enter()
        }
    }

    fun transfer2State(tag: String) { //shoule be in a thread?
        var state = getKeybyTag(tag)
        state?.let {
            transfer2State(it)
        }
        state?:let {
            throw NullPointerException()
        }

    }

    override fun put(key: IState, value: @Composable (() -> Unit)): @Composable (() -> Unit)? {
        tagMap.put(key.tag(),key)
        return super.put(key, value)
    }

    override fun putAll(m: Map<out IState, out @Composable (() -> Unit)>) {
        m.forEach { key,value ->
            put(key,value)
        }
    }

    override fun remove(key: IState): @Composable (() -> Unit)? {
        tagMap.remove(key.tag())
        return super.remove(key)
    }

    override fun remove(key: IState, value: @Composable (() -> Unit)): Boolean {
        tagMap.remove(key.tag())
        return super.remove(key, value)
    }

    override fun putIfAbsent(
        key: IState,
        value: @Composable (() -> Unit)
    ): @Composable (() -> Unit)? {
        tagMap.remove(key.tag())
        return super.putIfAbsent(key, value)
    }



    override fun get(key: IState): @Composable (() -> Unit)? {
        return super.get(key)
    }

    fun getKeybyTag(tag: String): IState? {
        return tagMap.get(tag)
    }

}