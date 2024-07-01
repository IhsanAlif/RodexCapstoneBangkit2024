package com.alice.rodexapp.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.alice.rodexapp.R
import com.alice.rodexapp.databinding.ActivityReportPageBinding
import com.alice.rodexapp.fragment.CameraFragment
import com.alice.rodexapp.fragment.MapsFragment
import java.io.File

class ReportPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imagePath = intent.getStringExtra(CameraFragment.EXTRA_CAMERA_IMAGE)
        if (!imagePath.isNullOrEmpty()) {
            val file = File(imagePath)
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            binding.imageCapture.setImageBitmap(bitmap)
            }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        val inspector = intent.getStringExtra("inspector")
        val roadName = intent.getStringExtra("roadName")
        val roadLength = intent.getStringExtra("roadLength")
        val roadSection = intent.getStringExtra("roadSection")
        val roadSurface = intent.getStringExtra("roadSurface")
        val photoPath = intent.getStringExtra(CameraFragment.EXTRA_CAMERA_IMAGE)

        val inspectorTextView = findViewById<TextView>(R.id.etInspector)
        val roadNameTextView = findViewById<TextView>(R.id.etRoadName)
        val roadLengthTextView = findViewById<TextView>(R.id.etRoadLength)
        val roadSectionTextView = findViewById<TextView>(R.id.etRoadSection)
        val roadSurfaceSpinner = findViewById<Spinner>(R.id.spRoadSurface)
        val imageCapture = findViewById<ImageView>(R.id.imageView)

        inspectorTextView.text = inspector
        roadNameTextView.text = roadName
        roadLengthTextView.text = roadLength
        roadSectionTextView.text = roadSection

        val roadSurfaceOptions = resources.getStringArray(R.array.road_surface_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roadSurfaceOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roadSurfaceSpinner.adapter = adapter

        if (roadSurface != null) {
            val position = roadSurfaceOptions.indexOf(roadSurface)
            if (position != -1) {
                roadSurfaceSpinner.setSelection(position)
            }
        }

        roadSurfaceSpinner.isEnabled = false
        inspectorTextView.isEnabled = false
        roadNameTextView.isEnabled = false
        roadLengthTextView.isEnabled = false
        roadSectionTextView.isEnabled = false

        if (!photoPath.isNullOrEmpty()) {
            imageCapture.visibility = View.VISIBLE
            val imgFile = File(photoPath)
            if (imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                imageCapture.setImageBitmap(bitmap)
            }
        } else {
            imageCapture.visibility = View.GONE
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container_map, MapsFragment::class.java, null)
            }
        }
    }
}
