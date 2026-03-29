package com.eathemeat.transdroid.main.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.eathemeat.transdroid.main.MainModel
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainModel: MainModel = viewModel(),
) {
    ConstraintLayout() {

    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TransDroidTheme {
        HomeScreen()

    }
}