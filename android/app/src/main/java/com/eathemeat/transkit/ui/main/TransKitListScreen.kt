package com.eathemeat.transkit.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.transkit.ui.main.theme.TransApplicationTheme

/**
 * author:PeterX
 * time:2024/4/13 0013
 */
@Composable
fun TransKitListScreen(modifier: Modifier = Modifier,viewModel:MainViewModel = viewModel()) {
    Column {
//        var count by rememberSaveable


    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TransApplicationTheme {
        TransKitListScreen()
    }
}