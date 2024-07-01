package com.alice.rodexapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alice.rodexapp.R
import com.alice.rodexapp.fragment.CameraFragment
import com.alice.rodexapp.fragment.MapsFragment

class InspectionStartActivity : AppCompatActivity() {

    private lateinit var btnEnd: Button
    private lateinit var mapFragmentContainer: View
    private var dX = 0f
    private var dY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspection_start)

        btnEnd = findViewById(R.id.btnEnd)
        mapFragmentContainer = findViewById(R.id.fragment_container_map)

        mapFragmentContainer.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
                    true
                }
                else -> false
            }
        }

        if (allPermissionsGranted()) {
            loadFragments()
        } else {
            requestPermissions()
        }

        btnEnd.setOnClickListener {
            navigateToReportPage()
        }
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        requestPermissionsLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true) {
                loadFragments()
            } else {
            }
        }

    private fun loadFragments() {
        loadFragment(CameraFragment(), R.id.fragment_container_camera)
        loadFragment(MapsFragment(), R.id.fragment_container_map)
    }

    private fun loadFragment(fragment: Fragment, containerId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    private fun navigateToReportPage() {
        val intent = Intent(this, ReportPageActivity::class.java)
        intent.putExtra("inspector", getIntent().getStringExtra("inspector"))
        intent.putExtra("roadName", getIntent().getStringExtra("roadName"))
        intent.putExtra("roadLength", getIntent().getStringExtra("roadLength"))
        intent.putExtra("roadSection", getIntent().getStringExtra("roadSection"))
        intent.putExtra("roadSurface", getIntent().getStringExtra("roadSurface"))
        startActivity(intent)
    }
}
