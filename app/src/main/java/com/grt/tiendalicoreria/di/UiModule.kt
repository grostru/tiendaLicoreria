package com.grt.tiendalicoreria.di

import com.grt.tiendalicoreria.ui.cart.CartViewModel
import com.grt.tiendalicoreria.ui.category.CategoryViewModel
import com.grt.tiendalicoreria.ui.chat.ChatViewModel
import com.grt.tiendalicoreria.ui.detail.DetailViewModel
import com.grt.tiendalicoreria.ui.home.HomeViewModel
import com.grt.tiendalicoreria.ui.licoreria.LicoreriaViewModel
import com.grt.tiendalicoreria.ui.login.LoginViewModel
import com.grt.tiendalicoreria.ui.main.MainViewModel
import com.grt.tiendalicoreria.ui.order.OrderViewModel
import com.grt.tiendalicoreria.ui.product.ProductViewModel
import com.grt.tiendalicoreria.ui.track.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
/**
 * Created por Gema Rosas Trujillo
 * 25/02/2022
 *
 * Modulo en el que iniciamos todos los ViewModel que vamos a usar como Injección de dependencias
 */
val uiModule = module {

    viewModel {
        MainViewModel()
    }

    viewModel {
        HomeViewModel()
    }

    viewModel {
        LicoreriaViewModel()
    }

    viewModel {
        LoginViewModel()
    }

    viewModel {
        CategoryViewModel(get())
    }

    viewModel {
        ProductViewModel(get())
    }

    viewModel {
        DetailViewModel(get())
    }

    viewModel {
        CartViewModel()
    }

    viewModel {
        OrderViewModel()
    }

    viewModel {
        TrackViewModel()
    }

    viewModel {
        ChatViewModel()
    }
}