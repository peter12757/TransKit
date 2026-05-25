package com.eathemeat.transdroid.main.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eathemeat.transdroid.R
import com.eathemeat.transdroid.main.MainModel
import com.eathemeat.transdroid.main.ui.screen.ble.BleScreen
import com.eathemeat.transdroid.main.ui.screen.net.NetScreen
import com.eathemeat.transdroid.main.ui.screen.other.OtherScreen
import com.eathemeat.transdroid.main.ui.screen.ray.RayScreen
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mainModel: MainModel = viewModel(),
) {
    val navController = rememberNavController()

    ConstraintLayout() {
        val (nav,host) = createRefs()
        LazyRow(modifier = modifier.fillMaxWidth()
            .height(60.dp)
            .background(Color.White).constrainAs(nav){
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            top.linkTo(host.bottom,5.dp)
        }) {
            items(mainModel.homeState.navItems){ item ->
                // TODO:  
                IconButton(onClick = {
                    navController.navigate(item.key)
                }) {
                    Image(painter = painterResource(item.icon), contentDescription = "bluetooth",
                        modifier = Modifier.size(200.dp), contentScale = ContentScale.Crop)
                    Text(
                        text = item.title, style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        NavHost(navController, startDestination = mainModel.homeState.curNavItem.title) {
            // TODO: bind item & screen
            composable("bluetooth") {
                BleScreen()
            }
            composable("net") {
                NetScreen()
            }
            composable("ray") {
               RayScreen()
            }
            composable("other") {
                OtherScreen()
            }
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