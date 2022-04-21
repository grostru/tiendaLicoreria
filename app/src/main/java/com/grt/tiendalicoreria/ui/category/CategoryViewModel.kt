package com.grt.tiendalicoreria.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.grt.tiendalicoreria.common.BaseViewModel
import com.grt.tiendalicoreria.common.NavData
import com.grt.tiendalicoreria.domain.model.CategoryModel
import com.grt.tiendalicoreria.domain.usecase.GetSavedCategorysUseCase

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class CategoryViewModel(
    private val getSavedCategorysUseCase: GetSavedCategorysUseCase
) : BaseViewModel() {

    companion object{
        const val NAV_CATEGORYS = 0
    }

    private val liveListCategorys: MutableLiveData<List<CategoryModel>> = MutableLiveData()
    val obsListCategorys: LiveData<List<CategoryModel>> = liveListCategorys

    // Función que obtiene la lista de Pokemons de BBDD para su posterior pintado
    override fun onInitialization() {
        executeUseCase {
            liveListCategorys.value = getSavedCategorysUseCase.execute(Unit)
        }
    }

    // Función encargada del manejo de haber hecho click en uno de los elementos de la lista de
    // Pokemon y navega hasta la pantalla de Detalles
    fun onActionCategoryClicked(categoryModel: CategoryModel) {
        navigate(NavData(NAV_CATEGORYS, categoryModel))
    }


}