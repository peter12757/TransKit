package com.eathemeat.transdroid.main.ui.icons.nav

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.eathemeat.transdroid.main.ui.icons.TransIcons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview

val TransIcons.NAV.BleIcon : ImageVector
    get() {
        if (_bleicon != null) {
            return _bleicon!!
        }
        // 将你的 SVG pathData 封装为 ImageVector
        _bleicon = ImageVector.Builder(
            defaultWidth = 576.dp, // 根据原始 viewBox 宽度设定
            defaultHeight = 480.dp, // 根据原始 viewBox 高度设定
            viewportWidth = 576f,
            viewportHeight = 480f
        ).addPath(

        )

            .path(
            fill = Color.Black, // 默认填充色，会被 Icon 的 tint 覆盖
            pathData = "M288 96c-90.9 0-173.2 36-233.7 94.6-12.7 12.3-33 12-45.2-.7s-12-33 .7-45.2C81.7 74.9 179.9 32 288 32S494.3 74.9 566.3 144.7c12.7 12.3 13 32.6 .7 45.2s-32.6 13-45.2 .7C461.2 132 378.9 96 288 96zM240 432a48 48 0 1 1 96 0 48 48 0 1 1 -96 0zM168 326.2c-11.7 13.3-31.9 14.5-45.2 2.8s-14.5-31.9-2.8-45.2C161 237.4 221.1 208 288 208s127 29.4 168 75.8c11.7 13.3 10.4 33.5-2.8 45.2s-33.5 10.4-45.2-2.8C378.6 292.9 335.8 272 288 272s-90.6 20.9-120 54.2z"
        ).build()



        return _bleicon!!
    }


private var _bleicon: ImageVector? = null