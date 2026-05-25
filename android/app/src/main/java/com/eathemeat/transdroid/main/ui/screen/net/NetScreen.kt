package com.eathemeat.transdroid.main.ui.screen.net

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.transdroid.main.MainModel
import com.eathemeat.transdroid.main.ui.screen.ble.BleScreen
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme

@Composable
fun NetScreen(
    modifier: Modifier = Modifier,
    mainModel: MainModel = viewModel(),
) {

}

@Preview(showBackground = true)
@Composable
fun NetScreenPreview() {
    TransDroidTheme {
        NetScreen()

    }
}

