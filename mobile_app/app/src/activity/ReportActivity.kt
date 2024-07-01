package com.alice.rodexapp.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alice.rodexapp.R
import com.alice.rodexapp.adapter.LoadingStateAdapter
import com.alice.rodexapp.adapter.ReportAdapter
import com.alice.rodexapp.databinding.ActivityReportBinding
import com.alice.rodexapp.viewmodel.MainViewModel
import com.alice.rodexapp.viewmodel.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar

class ReportActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private lateinit var binding: ActivityReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        showRecyclerList()
        setupToolbar()
        playAnimation()
    }

    private fun setupAction() {
        viewModel.getSession().observe(this) { session ->
            if (!session.isLogin) {
                // Redirect user to login if not logged in
            } else {
                binding.progressBar.visibility = View.GONE
                getStoriesData()
            }
        }
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
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

    private fun getStoriesData() {
        val adapter = ReportAdapter()
        adapter.removeDivider(binding.rvStory)
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.getStories().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    companion object {
        const val USER_ID = "user id"
    }
}
