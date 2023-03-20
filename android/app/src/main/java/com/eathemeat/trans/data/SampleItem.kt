package com.eathemeat.trans.data



enum class SampleState {
    TODO,
    DOING,
    FINISH,
}
data class SampleItem(val name:String, val icon:Int, val desc:String, val state:SampleState)
