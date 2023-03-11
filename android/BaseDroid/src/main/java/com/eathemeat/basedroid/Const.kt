package com.eathemeat.basedroid

class Const {

    enum class ErrorCode(val value:Int) {
        BLUE_UNABLE(-1);


        fun value(): Int {
            return value
        }
    }

    enum class BlueState(val value:Int) {
        STATE_IDLE(-1),
        STATE_SCANNING(0X01);

        fun value(): Int {
            return value
        }
    }
}