package com.eathemeat.transkit.bluetooth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
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
    

}

@Preview(showBackground = true)
@Composable
fun BluetoothScreenPreview() {
    TransApplicationTheme {
        BluetoothScreen()
    }
}