package com.alice.rodexapp.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alice.rodexapp.R
import com.bumptech.glide.Glide
import com.alice.rodexapp.databinding.ActivityDetailBinding
import com.alice.rodexapp.viewmodel.AboutRoad

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgPhoto: ImageView = findViewById(R.id.img_detail_photo)
        val tvDetailName: TextView = findViewById(R.id.tv_detail_name)
        val tvDetailDescription: TextView = findViewById(R.id.tv_detail_description)

        val dataAboutRoad = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("key_aboutRoad", AboutRoad::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("key_aboutRoad")
        }

        if (dataAboutRoad != null) {
            tvDetailName.text = dataAboutRoad.name
            tvDetailDescription.text = dataAboutRoad.description
            Glide.with(applicationContext)
                .load(dataAboutRoad.photo)
                .into(imgPhoto)
        }
        setupToolbar()
        playAnimation()
    }
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}