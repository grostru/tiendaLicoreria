package com.grt.tiendalicoreria.di

import com.grt.tiendalicoreria.domain.usecase.GetSavedCategorysUseCase
import com.grt.tiendalicoreria.domain.usecase.GetSavedProductsUseCase
import org.koin.dsl.module
/**
 * Created por Gema Rosas Trujillo
 * 28/01/2022
 *
 * Clase en la que definimos todos los casos de uso usados como injección de dependencias
 * en la app
 */
val usecaseModule = module {

    single { GetSavedCategorysUseCase(get()) }

    single { GetSavedProductsUseCase(get()) }

}



