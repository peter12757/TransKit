package com.eathemeat.transdroid.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.main.ui.screen.home.HomeScreen
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
    val navController = rememberNavController()
    NavHost(navController,startDestination ="launcher") {
        composable("launcher") {
            LauncherScreen(modifier, mainModel = viewModel(),navController)
        }
        composable("home") { HomeScreen() }

    }

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TransDroidTheme {
        MainScreen(viewModel = viewModel())

    }
}