package com.grt.tiendalicoreria.domain.repository

import com.grt.tiendalicoreria.domain.model.CategoryModel


/**
 * Created por Gema Rosas Trujillo
 * 25/03/2022
 * Interface del caso de uso que obtiene la lista de Categorias de Firebase
 */
interface GetCategorysRepository {
    suspend fun getCategorys(): Result<List<CategoryModel>>
}