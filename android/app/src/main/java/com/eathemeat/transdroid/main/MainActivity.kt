package com.eathemeat.transdroid.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("1231231")
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
    val state = viewModel.curState.collectAsState().value
    viewModel.screen(state)

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TransDroidTheme {
        MainScreen(viewModel = viewModel())

    }
}