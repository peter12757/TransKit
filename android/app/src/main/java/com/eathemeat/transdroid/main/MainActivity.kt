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
import com.eathemeat.transdroid.main.ui.screen.launcher.LauncherScreen
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("1231231")
        enableEdgeToEdge()
        setContent {
            TransDroidTheme {
                MainScreen(viewModel())
            }
        }
    }
}

@Composable
fun MainScreen(viewModel:MainModel,modifier: Modifier = Modifier) {
    LauncherScreen(modifier, mainModel = viewModel())
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TransDroidTheme {
        MainScreen(viewModel = viewModel())

    }
}