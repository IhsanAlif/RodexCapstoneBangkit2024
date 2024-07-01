package com.alice.rodexapp.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alice.rodexapp.R
import com.alice.rodexapp.adapter.AboutRoadAdapter
import com.alice.rodexapp.databinding.ActivityMainBinding
import com.alice.rodexapp.viewmodel.AboutRoad
import com.alice.rodexapp.viewmodel.MainViewModel
import com.alice.rodexapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAboutRoad: RecyclerView
    private val list = ArrayList<AboutRoad>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvAboutRoad = findViewById(R.id.rvAboutRoad)
        rvAboutRoad.setHasFixedSize(true)

        list.addAll(getListAboutRoad())
        showRecyclerList()
        setupAction()
        playAnimation()
    }


    private fun setupAction() {
        viewModel.getSession().observe(this) { session ->
            if (!session.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.menu.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.inspection -> {
                    val intent = Intent(this, InspectionActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.report -> {
                    val intent = Intent(this, ReportActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.profile -> {
                    val intent = Intent(this, ProfilActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }


    private fun getListAboutRoad(): ArrayList<AboutRoad> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listAboutRoad = ArrayList<AboutRoad>()
        for (i in dataName.indices) {
            val aboutRoad = AboutRoad(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listAboutRoad.add(aboutRoad)
        }
        return listAboutRoad
    }

    private fun showRecyclerList() {
        rvAboutRoad.layoutManager = LinearLayoutManager(this)
        val listAboutRoadAdapter = AboutRoadAdapter(list)
        rvAboutRoad.adapter = listAboutRoadAdapter

        listAboutRoadAdapter.setOnItemClickCallback(object : AboutRoadAdapter.OnItemClickCallback {
            override fun onItemClicked(data: AboutRoad) {
                showSelectedAboutRoad(data)
            }
        })
    }

    private fun showSelectedAboutRoad(aboutRoad: AboutRoad) {
        Toast.makeText(this, "Kamu memilih " + aboutRoad.name, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

}
