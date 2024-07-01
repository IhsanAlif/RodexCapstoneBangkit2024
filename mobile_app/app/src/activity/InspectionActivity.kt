package com.alice.rodexapp.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alice.rodexapp.R
import com.alice.rodexapp.databinding.ActivityInspectionBinding
import com.alice.rodexapp.utils.Result
import com.alice.rodexapp.viewmodel.DetailViewModel
import com.alice.rodexapp.viewmodel.ViewModelFactory

class InspectionActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityInspectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInspectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupSpinner()
        setupAction()
        playAnimation()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.road_surface_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRoadSurface.adapter = adapter
        }
    }

    private fun setupAction() {
        binding.btnStart.setOnClickListener {
            if (validateInput()) {
                saveData()
                val intent = Intent(this, InspectionStartActivity::class.java)
                intent.putExtra("inspector", binding.etInspector.text.toString().trim())
                intent.putExtra("roadName", binding.etRoadName.text.toString().trim())
                intent.putExtra("roadLength", binding.etRoadLength.text.toString().trim())
                intent.putExtra("roadSection", binding.etRoadSection.text.toString().trim())
                intent.putExtra("roadSurface", binding.spinnerRoadSurface.selectedItem.toString().trim())
                startActivity(intent)
            }
        }

        viewModel.inspectionData.observe(this) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    val data = result.data
                    binding.etInspector.setText(data.inspector)
                    binding.etRoadName.setText(data.roadName)
                    binding.etRoadLength.setText(data.roadLength.toString())
                    binding.etRoadSection.setText(data.roadSection.toString())

                    val roadSurfaceOptions = resources.getStringArray(R.array.road_surface_options)
                    val surfaceIndex = roadSurfaceOptions.indexOf(data.roadSurface)
                    if (surfaceIndex >= 0) {
                        binding.spinnerRoadSurface.setSelection(surfaceIndex)
                    }
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

        viewModel.getInspectionData()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun validateInput(): Boolean {
        val inspector = binding.etInspector.text.toString().trim()
        val roadName = binding.etRoadName.text.toString().trim()
        val roadLengthStr = binding.etRoadLength.text.toString().trim()
        val roadSectionStr = binding.etRoadSection.text.toString().trim()
        val roadSurface = binding.spinnerRoadSurface.selectedItem?.toString()?.trim()

        if (inspector.isEmpty() || roadName.isEmpty() || roadLengthStr.isEmpty() || roadSectionStr.isEmpty() || roadSurface.isNullOrEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        try {
            roadLengthStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Road Length must be a valid number", Toast.LENGTH_SHORT).show()
            return false
        }

        try {
            roadSectionStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Road Section must be a valid number", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveData() {
        val inspector = binding.etInspector.text.toString().trim()
        val roadName = binding.etRoadName.text.toString().trim()
        val roadLengthStr = binding.etRoadLength.text.toString().trim()
        val roadSectionStr = binding.etRoadSection.text.toString().trim()
        val roadSurface = binding.spinnerRoadSurface.selectedItem?.toString()?.trim()

        val roadLength = roadLengthStr.toInt()
        val roadSection = roadSectionStr.toInt()

        if (!roadSurface.isNullOrEmpty()) {
            viewModel.saveInspectionData(inspector, roadName, roadLength, roadSection, roadSurface)
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
        }
    }
}
