package com.alice.rodexapp.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alice.rodexapp.R
import com.alice.rodexapp.databinding.ActivityReportDetailBinding
import com.alice.rodexapp.utils.Result
import com.alice.rodexapp.viewmodel.DetailViewModel
import com.alice.rodexapp.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar

class ReportDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityReportDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupAction()
        setupToolbar()
        playAnimation()
    }

    private fun setupAction() {
        val userId = intent.getStringExtra(USER_ID)

        if (userId != null) {
            viewModel.getDetailStory(userId).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                    }

                    is Result.Success -> {
                        binding.tvName.text = result.data.story.name
                        binding.tvDesc.text = result.data.story.description
                        Glide.with(this)
                            .load(result.data.story.photoUrl)
                            .into(binding.ivPict)
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            application,
                            "Error: ${result.error}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    companion object {
        const val USER_ID = "user id"
    }
}
