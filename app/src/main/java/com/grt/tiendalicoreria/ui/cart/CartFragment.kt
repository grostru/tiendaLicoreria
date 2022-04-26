package com.grt.tiendalicoreria.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.grt.tiendalicoreria.data.Constants
import com.grt.tiendalicoreria.R
import com.grt.tiendalicoreria.common.BaseFragment
import com.grt.tiendalicoreria.data.firestore.OrderModelDB
import com.grt.tiendalicoreria.databinding.FragmentCartBinding
import com.grt.tiendalicoreria.domain.model.OrderModel
import com.grt.tiendalicoreria.domain.model.ProductModel
import com.grt.tiendalicoreria.domain.model.ProductOrderModel
import com.grt.tiendalicoreria.ui.order.OrderFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(), OnCartListener {

    override val vm: CartViewModel by sharedViewModel()

    private lateinit var adapter: ProductCartAdapter

    protected var totalPrice = 0.0

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCartBinding {
        return FragmentCartBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.onInitialization()

        setupBinding()
        setupRecyclerView()
        setupButtons()

    }

    private fun setupBinding(){

        var result = 0.0
        for (product in vm.obsListCartProducts.value!!){
            result += product.totalPrice()
        }

        binding.tvTotal.text = getString(R.string.product_full_cart, result)
        totalPrice = result

        observeData(vm.obsListCartProducts,::onObserveCartList)
    }

    private fun onObserveCartList(listaProductos: List<ProductModel>){
        adapter.updateList(listaProductos)
    }

    private fun setupRecyclerView() {
        binding?.let {
            adapter = ProductCartAdapter(mutableListOf(), this)

            it.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@CartFragment.adapter
            }
        }
    }

    // Método que realiza el pedido enviando un mensaje a la tienda y almacenando dicho pedido en firestore
    private fun setupButtons(){
        binding?.let {
            it.efab.setOnClickListener {
                requestOrder()
                //requestOrderTransaction()
            }
        }
    }

    private fun getProducts(){
        vm.obsListCartProducts.value?.toMutableList()?.forEach {
            adapter.add(it)
        }
    }

    private fun configAnalytics(){
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
            param(FirebaseAnalytics.Param.METHOD, "check_track")
        }
    }

    private fun requestOrderTransaction(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { myUser ->
            enableUI(false)

            val products = hashMapOf<String, ProductOrderModel>()
            adapter.getProducts().forEach { product ->
                products.put(product.id!!, ProductOrderModel(product.id!!, product.name!!, product.newQuantity))
            }

            val order = OrderModel(clientId = myUser.uid, products = products, totalPrice = totalPrice, status = 1)

            val db = FirebaseFirestore.getInstance()

            val requestDoc = db.collection(Constants.COLL_REQUESTS).document()
            val productsRef = db.collection(Constants.COLL_PRODUCTS)

            db.runBatch { batch ->
                batch.set(requestDoc, order)

                order.products.forEach {
                    batch.update(productsRef.document(it.key), Constants.PROP_QUANTITY,
                        FieldValue.increment(-it.value.quantity.toLong()))
                }
            }
                .addOnSuccessListener {
                    //dismiss()
                    vm.obsListCartProducts.value?.toMutableList()?.clear()
                    startActivity(Intent(context, OrderFragment::class.java))

                    Toast.makeText(activity, "Compra realizada.", Toast.LENGTH_SHORT).show()
                    //Analytics
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO){
                        val products = mutableListOf<Bundle>()
                        order.products.forEach {
                            if (it.value.quantity > 5) {
                                val bundle = Bundle()
                                bundle.putString("id_product", it.key)
                                products.add(bundle)
                            }
                        }
                        param(FirebaseAnalytics.Param.QUANTITY, products.toTypedArray())
                    }
                    firebaseAnalytics.setUserProperty(Constants.USER_PROP_QUANTITY,
                        if (products.size > 0) "con_mayoreo" else "sin_mayoreo")
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "Error al comprar.", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    enableUI(true)
                }
        }
    }

    private fun requestOrder(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { myUser ->
            enableUI(false)

            val products = hashMapOf<String, ProductOrderModel>()
            adapter.getProducts().forEach { product ->
                products.put(product.id!!, ProductOrderModel(product.id!!, product.name!!, product.newQuantity))
            }

            val order = OrderModelDB(clientId = myUser.uid, clientName = myUser.displayName, products = products, totalPrice = totalPrice, status = 1)

            val db = FirebaseFirestore.getInstance()
            db.collection(Constants.COLL_REQUESTS)
                .add(order)
                .addOnSuccessListener {

                    vm.obsListCartProducts.value?.toMutableList()?.clear()

                    Toast.makeText(activity, "Compra realizada correctamente.", Toast.LENGTH_LONG).show()

                    //Analytics
                    /*firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO){
                        val products = mutableListOf<Bundle>()
                        order.products.forEach {
                            if (it.value.quantity > 5) {
                                val bundle = Bundle()
                                bundle.putString("id_product", it.key)
                                products.add(bundle)
                            }
                        }
                        param(FirebaseAnalytics.Param.QUANTITY, products.toTypedArray())
                    }
                    firebaseAnalytics.setUserProperty(Constants.USER_PROP_QUANTITY,
                        if (products.size > 0) "con_mayoreo" else "sin_mayoreo")*/
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "Error al comprar.", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    enableUI(true)

                    findNavController().navigate(R.id.nav_order)
                }
        }
    }

    private fun enableUI(enable: Boolean){
        binding?.let {
            it.efab.isEnabled = enable
        }
    }

    override fun onDestroyView() {
        //vm.updateTotal()

        super.onDestroyView()
        //binding = null
    }

    override fun setQuantity(product: ProductModel) {
        adapter.update(product)
    }

    override fun showTotal(total: Double) {
        totalPrice = total
        binding?.let {
            it.tvTotal.text = getString(R.string.product_full_cart, total)
        }
    }
}