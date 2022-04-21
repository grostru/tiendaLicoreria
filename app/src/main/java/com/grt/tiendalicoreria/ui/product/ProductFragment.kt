package com.grt.tiendalicoreria.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.grt.tiendalicoreria.R
import com.grt.tiendalicoreria.common.BaseFragment
import com.grt.tiendalicoreria.common.NavData
import com.grt.tiendalicoreria.databinding.FragmentProductBinding
import com.grt.tiendalicoreria.domain.model.ProductModel
import com.grt.tiendalicoreria.ui.cart.CartViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class ProductFragment : BaseFragment<FragmentProductBinding, ProductViewModel>() {

    override val vm: ProductViewModel by sharedViewModel()

    val vmCart: CartViewModel by sharedViewModel()

    val args: ProductFragmentArgs by navArgs()

    private val productAdapter by lazy {
        ProductAdapter(){
            // Capturamos la acción de pulsar en un elemento de la lista
            vm.onActionProductClicked(it)
        }
    }

    private lateinit var firestoreListener: ListenerRegistration
    private var queryPagination: Query? = null

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProductBinding {
        return FragmentProductBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = args.category
        vm.onAttachProductsCategory(category)

        setupBinding()

        setupTotalCart()

        setupRecycler()

        setupButtons()

    }

    private fun setupTotalCart(){
        var result = 0.0
        for (product in vmCart.obsListCartProducts.value!!){
            result += product.totalPrice()
        }
        if (result == 0.0){
            binding.tvTotal.text = getString(R.string.product_empty_cart)
        } else {
            binding.tvTotal.text = getString(R.string.product_full_cart, result)
        }
    }

    private fun setupBinding() {
        observeData(vm.obsListProducts,::onObserveList)
    }

    private fun onObserveList(list: List<ProductModel>) {
        productAdapter.updateList(list)
    }

    private fun setupRecycler(){

        with(binding){
            rvCategorys.layoutManager = GridLayoutManager(requireContext(), 2,
                GridLayoutManager.HORIZONTAL, false)
            rvCategorys.adapter = productAdapter

            llProgress.visibility = View.GONE
            nsvCategorys.visibility = View.VISIBLE
        }
    }

    private fun setupButtons(){
        binding.btnViewCart.setOnClickListener {
            findNavController().navigate(ProductFragmentDirections.actionProductFragmentToCartFragment())
        }
    }

    override fun onNavigate(navData: NavData) {
        when(navData.id){
            ProductViewModel.NAV_DETAIL ->{

                var product = navData.data as ProductModel?
                val index = vmCart.obsListCartProducts.value?.indexOf(product)
                if (index != -1){
                    product = vmCart.obsListCartProducts.value?.get(index!!)
                }
                findNavController().navigate(ProductFragmentDirections.actionProductFragmentToDetailFragment(product!!))
            }
        }
    }
}