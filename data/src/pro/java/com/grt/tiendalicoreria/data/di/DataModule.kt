package com.grt.tiendalicoreria.data.di

import com.grt.tiendalicoreria.data.firestore.FirestoreCategoryRepository
import com.grt.tiendalicoreria.data.firestore.FirestoreProductsRepository
import com.grt.tiendalicoreria.domain.repository.GetCategorysRepository
import com.grt.tiendalicoreria.domain.repository.GetProductsRepository
import org.koin.dsl.module

/**
 * Created por Gema Rosas Trujillo
 * 28/01/2022
 *
 * Modulo de Datos del inyector de Dependencias
 */
val dataModule = module {

    single { FirestoreCategoryRepository(get()) }

    single { FirestoreProductsRepository(get()) }

    single<GetCategorysRepository>() {
       get<FirestoreCategoryRepository>()
    }

    single<GetProductsRepository>() {
        get<FirestoreProductsRepository>()
    }

}