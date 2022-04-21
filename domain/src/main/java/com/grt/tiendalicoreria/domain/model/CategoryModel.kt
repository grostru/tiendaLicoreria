package com.grt.tiendalicoreria.domain.model

import java.io.Serializable

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
data class CategoryModel(
    var id: String? = null,
    var name:String? = null,
    var imgUrl: String? = null
): Serializable
