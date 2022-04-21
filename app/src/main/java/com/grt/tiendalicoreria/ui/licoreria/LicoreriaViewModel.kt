package com.grt.tiendalicoreria.ui.licoreria

import androidx.lifecycle.MutableLiveData
import com.grt.tiendalicoreria.common.BaseViewModel

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class LicoreriaViewModel : BaseViewModel() {

    private val liveShowFab = MutableLiveData<Boolean>()
    val obsShowFab = liveShowFab

    override fun onInitialization() {}

    fun showFab(show:Boolean){
        liveShowFab.value = show
    }

    fun navigateBack(){
        liveNavigation.value = null
    }
}