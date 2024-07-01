package com.alice.rodexapp.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.alice.rodexapp.R
import com.alice.rodexapp.fragment.OnBoard1Fragment

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 6000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            // Replace the container with OnBoard1Fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, OnBoard1Fragment())
                .commit()
        }, SPLASH_TIME_OUT)
    }
}
