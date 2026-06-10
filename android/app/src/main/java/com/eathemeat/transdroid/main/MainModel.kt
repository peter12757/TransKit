package com.eathemeat.transdroid.main

import androidx.lifecycle.ViewModel
import com.eathemeat.base.util.Logger
import com.eathemeat.transdroid.main.ui.screen.home.HomeSate
import com.eathemeat.transdroid.main.ui.screen.advertisement.AdState
import com.eathemeat.transdroid.main.ui.screen.ble.BleModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainModel: ViewModel() {

    enum class ThemeMode { Dark, Light }
    data class TransferUiState(
        val themeMode: ThemeMode = ThemeMode.Dark,
        val bluetoothEnabled: Boolean = true,
    )

    private val _uiState = MutableStateFlow(TransferUiState())


    init {
        Logger.d()
    }

    val launcherState = AdState()
    val homeState = HomeSate()

    val bleModel = BleModel()
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()


    fun toggleTheme() {
        _uiState.value = _uiState.value.copy(
            themeMode = if (_uiState.value.themeMode == ThemeMode.Dark) ThemeMode.Light else ThemeMode.Dark
        )
    }


}