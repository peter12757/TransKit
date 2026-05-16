package com.eathemeat.transdroid.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.main.ui.screen.LauncherScreen
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("1231231")
        enableEdgeToEdge()
        setContent {
            TransDroidTheme {
                LauncherScreen(viewModel())
            }
        }
    }
}

