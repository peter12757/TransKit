package com.eathemeat.transkit.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    LazyColumn(modifier = modifier) {
        items(items = viewModel.transKitList) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ,onClick = { /*TODO*/ }) {
                Text(text = it.name)

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TransApplicationTheme {
        TransKitListScreen()
    }
}