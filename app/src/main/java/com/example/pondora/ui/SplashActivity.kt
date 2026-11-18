package com.example.pondora.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pondora.MainActivity
import com.example.pondora.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(2000)

            val prefs = getSharedPreferences("pondora_prefs", Context.MODE_PRIVATE)
            val isFirstTime = true

            if (isFirstTime) {
                prefs.edit().putBoolean("is_first_time", false).apply()
                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }
    }
}