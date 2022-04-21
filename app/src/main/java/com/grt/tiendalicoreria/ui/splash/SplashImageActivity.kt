package com.grt.tiendalicoreria.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.grt.tiendalicoreria.common.BaseActivity
import com.grt.tiendalicoreria.databinding.ActivitySplashImageBinding
import com.grt.tiendalicoreria.ui.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created por Gema Rosas Trujillo
 * 25/02/2022
 *
 * Activity creado para cargar inicialmente la app
 */
class SplashImageActivity : BaseActivity<ActivitySplashImageBinding>(ActivitySplashImageBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashImageActivity, LoginActivity::class.java))
            finish()
        }
    }
}