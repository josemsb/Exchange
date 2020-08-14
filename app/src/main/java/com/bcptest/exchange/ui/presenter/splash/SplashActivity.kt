package com.bcptest.exchange.ui.presenter.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bcptest.exchange.R
import com.bcptest.exchange.ui.presenter.exchange.ExchangeActivity

class SplashActivity : AppCompatActivity() {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        loadUI()
    }

    private fun loadUI() {
        val runnable = Runnable {
            startActivity(Intent(this, ExchangeActivity::class.java))
        }
        handler.postDelayed(runnable, 1500)
    }
}