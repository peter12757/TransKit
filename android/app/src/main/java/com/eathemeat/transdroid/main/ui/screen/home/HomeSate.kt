package com.eathemeat.transdroid.main.ui.screen.home

import androidx.compose.ui.graphics.ImageBitmap
import java.util.UUID

data class NavigitionItem(val title:String, val icon: ImageBitmap?= null)

class HomeSate {
    val navItems: MutableList<NavigitionItem> = mutableListOf<NavigitionItem>(
        NavigitionItem("蓝牙"),
        NavigitionItem("Wifi"),
        NavigitionItem("红外"),
        NavigitionItem("其他")
    )

    val curNavItem = navItems[1]


}