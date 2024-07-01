package com.alice.rodexapp.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alice.rodexapp.R
import com.alice.rodexapp.databinding.ActivityProfilBinding
import com.alice.rodexapp.viewmodel.MainViewModel
import com.alice.rodexapp.viewmodel.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar

class ProfilActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityProfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupToolbar()
        playAnimation()
    }

    private fun setupAction() {
        binding.logout.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(R.string.message)
                setMessage(R.string.ask_logout)
                setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.logout()
                    finish()
                }
                setNegativeButton(R.string.no) { _, _ -> }
                create()
                show()
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
}
