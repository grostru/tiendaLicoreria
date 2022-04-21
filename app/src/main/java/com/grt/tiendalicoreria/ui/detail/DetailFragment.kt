package com.grt.tiendalicoreria.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.grt.tiendalicoreria.R
import com.grt.tiendalicoreria.common.BaseFragment
import com.grt.tiendalicoreria.databinding.FragmentDetailBinding
import com.grt.tiendalicoreria.domain.model.ProductModel
import com.grt.tiendalicoreria.ui.cart.CartViewModel
import com.grt.tiendalicoreria.ui.cart.ProductCartAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>() {

    override val vm: DetailViewModel by sharedViewModel()

    val vmCart: CartViewModel by sharedViewModel()

    val args: DetailFragmentArgs by navArgs()

    private lateinit var adapter: ProductCartAdapter

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupProduct(product)

        setupButtons(product)

    }

    private fun setupBinding(){
        observeData(vmCart.obsListCartProducts,::onObserveCartList)
    }

    private fun onObserveTotalCart(total: Double){
        vmCart.obsTotalCart.value = total
    }

    private fun onObserveCartList(listaProductos: List<ProductModel>){
        adapter.updateList(listaProductos)
    }

    private fun setupProduct(product: ProductModel) {
        product?.let { product ->
            binding?.let { binding ->
                binding.tvName.text = product.name
                binding.tvDescription.text = product.description
                binding.tvQuantity.text = getString(R.string.detail_quantity, product.quantity)
                setNewQuantity(product)

                Glide.with(this)
                    .load(product.imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_access_time)
                    .error(R.drawable.ic_broken_image)
                    .into(binding.imgProduct)
            }
        }
    }

    private fun setNewQuantity(product: ProductModel) {
        binding?.let {
            it.etNewQuantity.setText(product.newQuantity.toString())

            val newQuantityStr = getString(
                R.string.detail_total_price, product.totalPrice(),
                product.newQuantity, product.price)
            it.tvTotalPrice.text = HtmlCompat.fromHtml(newQuantityStr, HtmlCompat.FROM_HTML_MODE_LEGACY)

            vmCart.obsTotalCart?.value = product.totalPrice()
        }
    }

    private fun setupButtons(product: ProductModel){
        product?.let { product ->
            binding?.let { binding ->
                binding.ibSub.setOnClickListener {
                    if (product.newQuantity > 1){
                        product.newQuantity -= 1
                        setNewQuantity(product)
                    }
                }
                binding.ibSum.setOnClickListener {
                    if (product.newQuantity < product.quantity){
                        product.newQuantity += 1
                        setNewQuantity(product)
                    }
                }
                binding.efab.setOnClickListener {
                    product.newQuantity = binding.etNewQuantity.text.toString().toInt()
                    addToCart(product)
                }
            }
        }
    }

    private fun addToCart(product: ProductModel) {
        vmCart.addProductToCart(product)
        findNavController().navigate(R.id.nav_category)
    }
}