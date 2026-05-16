package com.eathemeat.transdroid.main.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eathemeat.transdroid.main.MainModel
import com.eathemeat.transdroid.main.ui.screen.home.HomeScreen
import com.eathemeat.transdroid.main.ui.screen.advertisement.AdScreen
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme

@Composable
fun LauncherScreen(viewModel:MainModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController,startDestination ="advertisement") {
        composable("advertisement") {
            AdScreen(modifier, onFinish = {
                navController.navigate("home")
            }, mainModel = viewModel())
        }
        composable("home") { HomeScreen(modifier,mainModel = viewModel()) }
    }

}

@Preview(showBackground = true)
@Composable
fun LauncherScreenPreview() {
    TransDroidTheme {
        LauncherScreen(viewModel = viewModel())

    }
}