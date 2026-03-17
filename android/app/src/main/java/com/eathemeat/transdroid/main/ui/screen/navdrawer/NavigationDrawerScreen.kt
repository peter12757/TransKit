package com.eathemeat.transdroid.main.ui.screen.navdrawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eathemeat.transdroid.main.MainModel


//https://developer.android.google.cn/develop/ui/compose/components/drawer?hl=zh_cn

@Composable
fun NavDrawerScreen(modifier: Modifier = Modifier, mainModel: MainModel = viewModel()) {

}


@Preview(showBackground = true)
@Composable
fun NavDrawerScreenPreview(modifier: Modifier = Modifier, mainModel: MainModel = viewModel()) {
    NavDrawerScreen(modifier,mainModel)
}