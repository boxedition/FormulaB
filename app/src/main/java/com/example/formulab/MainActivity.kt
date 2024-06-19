package com.example.formulab

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.formulab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var view: ActivityMainBinding

    //State of Permissions
    private var hasCameraPermission = false
    private var hasStoragePermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        checkPermissions()
    }

    private fun checkPermissions() {
        //Check initial permissions
        for (permission in REQUIRED_PERMISSIONS) {
            when (permission) {
                //TODO:
                //For future permissions duplicate this snippet and change type of permission and corresponded val
                Manifest.permission.CAMERA -> {
                    hasCameraPermission =
                        ContextCompat.checkSelfPermission(
                            this,
                            permission
                        ) == PackageManager.PERMISSION_GRANTED
                }

            }
        }



        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS.toTypedArray(),
            REQUIRED_PERMISSIONS.map { PERMISSION_REQUEST_CODES[it]!! }.maxOrNull()!!
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasCameraPermission = true
                    //updateUI()
                }
            }

            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasStoragePermission = true
                    //updateUI()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        private const val STORAGE_PERMISSION_REQUEST_CODE = 1002

        private val REQUIRED_PERMISSIONS = mutableListOf<String>().apply {
            add(Manifest.permission.CAMERA)
            // Storage permissions based on Android version
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    // Android 13 and later
                    addAll(
                        listOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_MEDIA_LOCATION,
                            Manifest.permission.READ_MEDIA_IMAGES
                        )
                    )
                }

                Build.VERSION.SDK_INT in Build.VERSION_CODES.Q..Build.VERSION_CODES.S_V2 -> {
                    // Android 10 to Android 12
                    addAll(
                        listOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_MEDIA_LOCATION
                        )
                    )
                }

                else -> {
                    // Prior to Android 10
                    addAll(
                        listOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    )
                }
            }
        }

        //Map of Common request codes
        private val COMMON_PERMISSION_REQUEST_CODES = mapOf(
            Manifest.permission.CAMERA to CAMERA_PERMISSION_REQUEST_CODE
        )

        //Map of Storage request codes
        private val STORAGE_PERMISSION_REQUEST_CODES = when {
            //Storage Request codes based of Android Version
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> mapOf(
                // Android 13 and later
                Manifest.permission.WRITE_EXTERNAL_STORAGE to STORAGE_PERMISSION_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE to STORAGE_PERMISSION_REQUEST_CODE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE to STORAGE_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_MEDIA_LOCATION to STORAGE_PERMISSION_REQUEST_CODE,
                Manifest.permission.READ_MEDIA_IMAGES to STORAGE_PERMISSION_REQUEST_CODE
            )

            Build.VERSION.SDK_INT in Build.VERSION_CODES.Q..Build.VERSION_CODES.S -> mapOf(
                // Android 10 to Android 12
                Manifest.permission.WRITE_EXTERNAL_STORAGE to STORAGE_PERMISSION_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE to STORAGE_PERMISSION_REQUEST_CODE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE to STORAGE_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_MEDIA_LOCATION to STORAGE_PERMISSION_REQUEST_CODE
            )

            else -> mapOf(
                // Prior to Android 10
                Manifest.permission.WRITE_EXTERNAL_STORAGE to STORAGE_PERMISSION_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE to STORAGE_PERMISSION_REQUEST_CODE
            )
        }

        //Bundle Permission request codes
        private val PERMISSION_REQUEST_CODES = mutableMapOf<String, Int>().apply {
            putAll(COMMON_PERMISSION_REQUEST_CODES)
            putAll(STORAGE_PERMISSION_REQUEST_CODES)
        }

    }
}