package com.eathemeat.transdroid.main.ui.screen.navdrawer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.main.MainModel


//https://developer.android.google.cn/develop/ui/compose/components/drawer?hl=zh_cn

@Composable
fun NavDrawerScreen(modifier: Modifier = Modifier, mainModel: MainModel = viewModel()) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet() {
                Text(text = "菜单",modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(text = "蓝牙")
                    },selected = true, onClick = {
                        Logger.d("")
                    }
                )
            }
        }
    ) {
        
    }
}


@Preview(showBackground = true)
@Composable
fun NavDrawerScreenPreview(modifier: Modifier = Modifier, mainModel: MainModel = viewModel()) {
    NavDrawerScreen(modifier,mainModel)
}