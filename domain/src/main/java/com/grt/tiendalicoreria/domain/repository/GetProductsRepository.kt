package com.grt.tiendalicoreria.domain.repository

import com.grt.tiendalicoreria.domain.model.CategoryModel
import com.grt.tiendalicoreria.domain.model.ProductModel


/**
 * Created por Gema Rosas Trujillo
 * 29/03/2022
 * Interface del caso de uso que obtiene la lista de Categorias de Firebase
 */
interface GetProductsRepository {
    suspend fun getProducts(categoryModel: CategoryModel): Result<List<ProductModel>>
}