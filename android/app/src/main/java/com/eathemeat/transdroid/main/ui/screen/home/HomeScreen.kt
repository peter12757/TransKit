package com.eathemeat.transdroid.main.ui.screen.home

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.eathemeat.transdroid.main.MainModel
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainModel: MainModel = viewModel(),
) {
    val navController = rememberNavController()

    ConstraintLayout() {
        val (nav,host) = createRefs()
        LazyRow(modifier = modifier.constrainAs(nav){
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            top.linkTo(host.bottom,5.dp)
        }) {
            items(mainModel.homeState.navItems){ item ->
                // TODO:  
                Card() { }
            }
        }
        NavHost(navController, startDestination = mainModel.homeState.curNavItem.title) {
            // TODO:
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TransDroidTheme {
        HomeScreen()

    }
}