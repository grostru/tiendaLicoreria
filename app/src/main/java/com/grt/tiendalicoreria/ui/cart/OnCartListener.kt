package com.grt.tiendalicoreria.ui.cart

import com.grt.tiendalicoreria.domain.model.ProductModel

/****
 * Project: Nilo
 * From: com.cursosandroidant.nilo.cart
 * Created by Alain Nicolás Tello on 08/06/21 at 15:32
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
interface OnCartListener {
    fun setQuantity(product: ProductModel)
    fun showTotal(total: Double)
}