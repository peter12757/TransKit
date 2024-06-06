package com.eathemeat.transkit.bluetooth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.bluetools.RemoteDevice
import com.eathemeat.transkit.R
import com.eathemeat.transkit.bluetooth.ui.theme.TransApplicationTheme

/**
 * author:PeterX
 * time:2024/5/15 0015
 */
@Composable
fun BluetoothDiscoveryScreen(
    modifier: Modifier = Modifier,
    viewModel: BluetoothViewModel = viewModel()


) {
    var showRefresh = remember {
        mutableStateOf(true)
    }

    var  enableBluetooth = remember {
       viewModel.enableBluetooth
    }
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (title, switch, refresh, list) = createRefs()
        Text(text = "蓝牙", fontSize = 8.em, modifier = Modifier
            .constrainAs(title) {
                start.linkTo(parent.start, margin = 10.dp)
            }
            .padding(5.dp))

        Switch(checked = enableBluetooth.value, onCheckedChange = {
            viewModel.enableBluetooth(enableBluetooth.value)
        }, modifier = Modifier
            .constrainAs(switch) {
                top.linkTo(title.top)
                bottom.linkTo(title.bottom)
                if (showRefresh.value) {
                    end.linkTo(refresh.start, margin = 10.dp)
                } else {
                    end.linkTo(parent.end, margin = 10.dp)
                }
            })
        if (showRefresh.value) {
            OutlinedButton(modifier = Modifier
                .constrainAs(refresh) {
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    end.linkTo(parent.end, margin = 10.dp)
                }, onClick = {
                    viewModel.startDiscovery()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_refresh),
                    contentDescription = "刷新",
                )
            }
        }
        LazyColumn(modifier = Modifier.constrainAs(list) {
            top.linkTo(title.bottom, margin = 10.dp)
            centerHorizontallyTo(parent)
        }
        ) {
            items(items = viewModel.devices) {
                BluetoothDeviceScreen(it)
            }
        }
    }
}

@Composable
fun BluetoothDeviceScreen(device: RemoteDevice) {
    ConstraintLayout(modifier = Modifier.clickable(enabled = true) {  }) {
        val (name, id) = createRefs()
        Text(text = device.name(),
            fontSize = 18.em
            , modifier = Modifier.constrainAs(name) {
            start.linkTo(parent.start, margin = 10.dp)
        })

        Text(text = device.id(),
            fontSize = 15.em
            , modifier = Modifier.constrainAs(id) {
            start.linkTo(name.start)
            top.linkTo(name.bottom, margin = 5.dp)
        })

    }
}

@Preview(showBackground = true)
@Composable
fun BluetoothDeviceScreenPreview() {
    TransApplicationTheme {
        BluetoothDeviceScreen(RemoteDevice())
    }
}

@Preview(showBackground = true)
@Composable
fun BluetoothDiscoveryScreenPreview() {
    TransApplicationTheme {
        BluetoothDiscoveryScreen()
    }
}

