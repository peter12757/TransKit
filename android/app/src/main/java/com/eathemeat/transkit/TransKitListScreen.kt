package com.eathemeat.transkit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eathemeat.transkit.ui.theme.TransApplicationTheme

/**
 * author:PeterX
 * time:2024/4/13 0013
 */
@Composable
fun TransKitListScreen(modifier: Modifier = Modifier) {
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TransApplicationTheme {
        TransKitListScreen()
    }
}