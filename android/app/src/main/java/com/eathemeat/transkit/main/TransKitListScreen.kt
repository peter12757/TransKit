package com.eathemeat.transkit.main

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.transkit.bluetooth.BluetoothActivity
import com.eathemeat.transkit.main.ui.theme.TransApplicationTheme

/**
 * author:PeterX
 * time:2024/4/13 0013
 */
@Composable
fun TransKitListScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()
                       , onClick:(item:TransKitItem)->Unit) {

    ConstraintLayout {
        val (title,line1,list) = createRefs()
        Text(text = "传输工具", modifier = Modifier.constrainAs(title) {
            top.linkTo(parent.top)
            start.linkTo(parent.start, margin = 5.dp)
        })
        HorizontalDivider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.constrainAs(line1) {
            top.linkTo(title.bottom)
            start.linkTo(parent.start, margin = 3.dp)
            end.linkTo(parent.end, margin = 3.dp)
        })
        LazyColumn(modifier = Modifier.constrainAs(list){
            top.linkTo(line1.bottom, margin = 5.dp)
            start.linkTo(parent.start)
        }) {
            items(items = viewModel.transKitList) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                    ,onClick = {
                        onClick(it)
                    }) {
                    TransItemScreen(item = it)
                }
            }
        }
    }

}

@Composable
fun TransItemScreen(item: TransKitItem) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (title,desc) = createRefs()
        Text(text = item.name, fontSize = 5.em, modifier = Modifier.constrainAs(title){
            top.linkTo(parent.top, margin = 5.dp)
            start.linkTo(parent.start,margin = 3.dp)
            end.linkTo(parent.end,margin = 5.dp)
        })
        Text(text = item.desc, fontSize = 3.em, modifier = Modifier.constrainAs(desc) {
            top.linkTo(title.bottom)
            start.linkTo(title.start)
            end.linkTo(parent.end,margin = 5.dp)
            bottom.linkTo(parent.bottom,margin = 5.dp)
        })
    }

}

@Preview(showBackground = true)
@Composable
fun TransItemScreenPreview() {
    TransItemScreen(TransKitItem("蓝牙传输(TraditionBluetooth)","使用传统蓝牙进行传输"
        ,BluetoothActivity::class.java) {
        Log.d("TAG", "TransItemScreenPreview: ")
    })
}


@Preview(showBackground = true)
@Composable
fun TransKitListScreenPreview() {
    TransApplicationTheme {
        TransKitListScreen() {
            Log.d("TAG", "TransKitListScreenPreview: ")
        }
    }
}