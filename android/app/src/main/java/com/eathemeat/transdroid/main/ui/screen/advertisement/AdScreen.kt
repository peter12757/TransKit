package com.eathemeat.transdroid.main.ui.screen.advertisement

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.R
import com.eathemeat.transdroid.main.MainModel
import com.eathemeat.transdroid.main.ui.theme.TransDroidTheme


@Composable
fun AdScreen(
    modifier: Modifier = Modifier,
    onFinish: ()-> Unit,
    mainModel: MainModel = viewModel(),
) {

    ConstraintLayout() {
        val state:AdState = mainModel.launcherState
        val time = state.launcherTime.collectAsState()

        if (time.value == 0) {
            Logger.d("trans to HomeScreen")
            onFinish()
        }
        Image(painter = painterResource(id = R.drawable.img_launcher), contentDescription = stringResource(R.string.img_launcher),modifier= Modifier.constrainAs(createRef()){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        })


        Text("${time.value/1000} S", color = Color.White, modifier = Modifier.constrainAs(createRef()) {
            top.linkTo(parent.top, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
        })
        state.startTime()
    }
}

@Preview(showBackground = true)
@Composable
fun AdScreenPreview() {
    TransDroidTheme {
        AdScreen(onFinish = {
            Logger.d("AdScreenPreview")
        })

    }
}