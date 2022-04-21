package com.grt.tiendalicoreria.ui.category

import android.os.Bundle
import android.view.*
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.grt.tiendalicoreria.R
import com.grt.tiendalicoreria.common.BaseFragment
import com.grt.tiendalicoreria.common.NavData
import com.grt.tiendalicoreria.databinding.FragmentCategoryBinding
import com.grt.tiendalicoreria.domain.model.CategoryModel
import com.grt.tiendalicoreria.ui.cart.CartViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class CategoryFragment : BaseFragment<FragmentCategoryBinding, CategoryViewModel>() {

    override val vm: CategoryViewModel by sharedViewModel()

    val vmCart: CartViewModel by sharedViewModel()

    private val categoryAdapter by lazy {
        CategoryAdapter(){
            // Capturamos la acción de pulsar en un elemento de la lista
            vm.onActionCategoryClicked(it)
        }
    }

    private lateinit var firestoreListener: ListenerRegistration
    private var queryPagination: Query? = null

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCategoryBinding {
        return FragmentCategoryBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.onInitialization()

        setupBinding()

        setupTotalCart()

        setupRecycler()

        setupButtons()
    }

    private fun setupBinding() {
        observeData(vm.obsListCategorys,::onObserveList)
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

    private fun onObserveList(list: List<CategoryModel>) {
        categoryAdapter.updateList(list)
    }

    private fun setupRecycler(){

        with(binding){
            rvCategorys.layoutManager = GridLayoutManager(requireContext(), 2,
                GridLayoutManager.HORIZONTAL, false)
            rvCategorys.adapter = categoryAdapter

            llProgress.visibility = View.GONE
            nsvCategorys.visibility = View.VISIBLE
        }
    }

    override fun onNavigate(navData: NavData) {
        when(navData.id){
            CategoryViewModel.NAV_CATEGORYS ->{

                val category = navData.data as CategoryModel

                findNavController().navigate(CategoryFragmentDirections.actionNavCategoryToProductFragment(category))
            }
        }
    }

    private fun setupButtons(){
        binding.btnViewCart.setOnClickListener {
            findNavController().navigate(CategoryFragmentDirections.actionNavCategoryToCartFragment())
        }
    }
}