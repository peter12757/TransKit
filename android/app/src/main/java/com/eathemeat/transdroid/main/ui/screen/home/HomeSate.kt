package com.eathemeat.transdroid.main.ui.screen.home

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import com.eathemeat.transdroid.R
import java.util.UUID

data class NavigitionItem(val key:String,  val title:String, val icon: Int= 0)

class HomeSate {
    val navItems: MutableList<NavigitionItem> = mutableListOf<NavigitionItem>(
        NavigitionItem("bluetooth","蓝牙", icon = R.drawable.icon_bluetooth),
        NavigitionItem("net","网络",icon = R.drawable.icon_bluetooth),
        NavigitionItem("ray","红外",icon = R.drawable.icon_bluetooth),
        NavigitionItem("other","其他",icon = R.drawable.icon_bluetooth)
    )

    val curNavItem = navItems[1]


}