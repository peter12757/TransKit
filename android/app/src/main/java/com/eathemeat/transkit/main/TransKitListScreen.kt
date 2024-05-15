package com.eathemeat.transkit.main

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
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
    LazyColumn(modifier = modifier.fillMaxWidth(1f)) {
        items(items = viewModel.transKitList) {
            Card(modifier = modifier.fillMaxWidth(1f).padding(5.dp),colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ,onClick = {
                    onClick(it)
                }) {
                TransItemScreen(item = it)
            }
        }
    }
}

@Composable
fun TransItemScreen(item: TransKitItem) {
    Column {
        Text(text = item.name, fontSize = 5.em)
        Text(text = item.desc, fontSize = 3.em)
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