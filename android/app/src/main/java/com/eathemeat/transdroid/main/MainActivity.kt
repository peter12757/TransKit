package com.eathemeat.transdroid.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TransDroidTheme {
                MainScreen(ViewModelProvider(MainActivity@ this).get(MainModel::class.java))

            }
        }
    }
}

@Composable
fun MainScreen(viewModel:MainModel,modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TransDroidTheme {
        MainScreen(viewModel = MainModel())

    }
}