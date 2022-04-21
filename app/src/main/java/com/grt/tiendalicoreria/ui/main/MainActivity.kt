package com.grt.tiendalicoreria.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.firebase.ui.auth.AuthUI
import com.grt.tiendalicoreria.R
import com.grt.tiendalicoreria.common.BaseActivity
import com.grt.tiendalicoreria.databinding.ActivityMainBinding

/**
 * Created por Gema Rosas Trujillo
 * 24/03/2022
 */
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        setSupportActionBar(binding.appBarMain.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_licoreria, R.id.nav_category, R.id.nav_enoturismo, R.id.nav_tintilla, R.id.nav_contact
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun showLoading() {
        binding.appBarMain.flMainLoading.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.appBarMain.flMainLoading.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_sign_out -> {
                this?.let { context ->
                    AuthUI.getInstance().signOut(context)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Sesión Terminada.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                this.onBackPressed()
                            } else {
                                Toast.makeText(context, "No se pudo cerrar la sesión.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            R.id.action_order_history -> {
                this.findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_order)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}