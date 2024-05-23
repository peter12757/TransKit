package com.eathemeat.transkit.bluetooth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.bluetools.RemoteDevice
import com.eathemeat.transkit.bluetooth.ui.theme.TransApplicationTheme


/**
 * author:PeterX
 * time:2024/5/17 0017
 */
@Composable
fun BluetoothScreen(
    modifier: Modifier = Modifier,
    viewModel: BluetoothViewModel = viewModel()
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (title,state,btn,content) = createRefs()
        Text(text = viewModel.currDevice.value.name, modifier = Modifier.constrainAs(title){
            start.linkTo(parent.start, margin = 10.dp)
            top.linkTo(parent.top, margin = 10.dp)
        })
        Text(text = viewModel.deviceState.value.name, modifier = Modifier.constrainAs(state){
            start.linkTo(title.end, margin = 10.dp)
            top.linkTo(parent.top, margin = 10.dp)
        })
        Button(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(btn) {
            top.linkTo(parent.top, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
            start.linkTo(state.end, margin = 10.dp)
        }) {
            Text(text = when(viewModel.deviceState.value) {
                BluetoothViewModel.Companion.DeviceState.Connected -> "连接"
                else -> "断开"
            },modifier = Modifier.padding(10.dp))
        }
        when(viewModel.currDevice.value.type) {
            RemoteDevice.Type.TYPE_BLUETOOTH -> BluetoothTestScreen(modifier = Modifier.constrainAs(content) {
                top.linkTo(title.bottom, margin = 10.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, viewModel = viewModel)
            RemoteDevice.Type.TYPE_BLE ->   BluetoothBLETestScreen(modifier = Modifier.constrainAs(content) {
                top.linkTo(title.bottom, margin = 10.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },viewModel = viewModel)
            RemoteDevice.Type.TYPE_BLEAUDIO ->   BluetoothBLEAudioTestScreen(modifier = Modifier.constrainAs(content) {
                top.linkTo(title.bottom, margin = 10.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },viewModel = viewModel)
            RemoteDevice.Type.TYPE_UNKONWN ->   ErrorScreen(modifier = Modifier.constrainAs(content) {
                top.linkTo(title.bottom, margin = 10.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                },viewModel = viewModel)
        }
    }

}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier,viewModel: BluetoothViewModel) {
    ConstraintLayout {
        Text(text = "Error", modifier = Modifier.constrainAs(createRef()) {
            centerTo(parent)
        })
    }
}

@Composable
fun BluetoothBLEAudioTestScreen(modifier: Modifier = Modifier,viewModel: BluetoothViewModel) {
    TODO("Not yet implemented")
}

@Composable
fun BluetoothBLETestScreen(modifier: Modifier = Modifier,viewModel: BluetoothViewModel) {
    TODO("Not yet implemented")
}

@Composable
fun BluetoothTestScreen(modifier: Modifier = Modifier,viewModel: BluetoothViewModel) {


}

@Preview(showBackground = true, widthDp = 720, heightDp = 1080)
@Composable
fun BluetoothScreenPreview() {
    TransApplicationTheme {
        BluetoothScreen()
    }
}