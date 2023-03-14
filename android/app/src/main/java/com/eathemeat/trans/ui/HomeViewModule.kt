package com.eathemeat.trans.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.eathemeat.trans.data.AbilityItem

class HomeViewModule : ViewModel() {

    val ability:MutableLiveData<MutableList<AbilityItem>> = MutableLiveData<MutableList<AbilityItem>>()


}