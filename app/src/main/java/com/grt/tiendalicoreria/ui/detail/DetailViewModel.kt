package com.grt.tiendalicoreria.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.grt.tiendalicoreria.common.BaseViewModel
import com.grt.tiendalicoreria.common.NavData
import com.grt.tiendalicoreria.domain.model.CategoryModel
import com.grt.tiendalicoreria.domain.model.ProductModel
import com.grt.tiendalicoreria.domain.usecase.GetSavedProductsUseCase

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class DetailViewModel(
    private val getSavedProductsUseCase: GetSavedProductsUseCase
) : BaseViewModel() {

    companion object{
        const val NAV_DETAIL = 0
    }

    private val liveListProducts: MutableLiveData<List<ProductModel>> = MutableLiveData()
    val obsListProducts: LiveData<List<ProductModel>> = liveListProducts

    // Función que obtiene la lista de Pokemons de BBDD para su posterior pintado
    fun onAttachProductsCategory(categoryModel: CategoryModel) {
        executeUseCase {
            liveListProducts.value = getSavedProductsUseCase.execute(categoryModel)
        }
    }

    // Función encargada del manejo de haber hecho click en uno de los elementos de la lista de
    // Pokemon y navega hasta la pantalla de Detalles
    fun onActionProductClicked(productModel: ProductModel) {
        navigate(NavData(NAV_DETAIL, productModel))
    }

    override fun onInitialization() {}

}