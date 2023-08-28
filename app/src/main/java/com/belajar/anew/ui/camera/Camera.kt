package com.belajar.anew.ui.camera

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.belajar.MyCatalog.databinding.ActivityCameraBinding
import com.belajar.anew.ui.form.EditProfileActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Camera : AppCompatActivity() {
    //Mendeklarasikan variable binding untuk mereferensikan Binding
    //Mendeklarasikan variable binding untuk mereferensikan imgCapture
    //Mendeklarasikan variable binding untuk mereferensikan cameraExecutor
    private lateinit var binding: ActivityCameraBinding
    private var imgCapture : ImageCapture? = null
    private lateinit var cameraExecutor : ExecutorService

    //kondisi awal saat MainActivity baru diciptakan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //request camera permission
        if (allPermissionsGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(
                this,
                EditProfileActivity.REQUIRED_PERMISSIONS,
                EditProfileActivity.REQUEST_CODE_PERMISSIONS
            )
        }

        //set uo listener untuk tindakan yang akan dilakukan ketika di klik
        binding.imageCaptureButton.setOnClickListener{takePhoto()}

        cameraExecutor = Executors.newSingleThreadExecutor()


    }
    //memulai camera
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            //untuk menghidupkn camera saat dijalankan
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Image
            imgCapture = ImageCapture.Builder().build()


            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // melepas tindakan sebelum memasang ulang
                cameraProvider.unbindAll()

                // memasang penggunaan ke kamera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,preview, imgCapture)

            } catch(exc: Exception) {
                Log.e(EditProfileActivity.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    //izin
    private fun allPermissionsGranted()= EditProfileActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        )== PackageManager.PERMISSION_GRANTED
    }

    //foto
    private fun takePhoto() {
        // untuk dapat referensi stabil dari pengambilan gambar yang dapat dimodifikasi
        val imageCapture = imgCapture ?: return

        //  untuk buat nama dengan waktu dan entri MediaStore.
        val name = SimpleDateFormat(EditProfileActivity.FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Buat objek opsi output yang isinya file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // untuk pengambilan gambar, yang dipicu setelah foto
        // berhasil diambil gambar
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(EditProfileActivity.TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    val intent = Intent()
                    intent.putExtra(RESULT_KEY,output.savedUri.toString())
                    setResult(RESULT_CODE, intent)
                    finish()
                    Log.d(EditProfileActivity.TAG, msg)
                }
            }
        )
    }

    //untuk mengahancurkan
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    // untuk menangani hasil dari permintaan izin yang diajukan
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EditProfileActivity.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object{
        const val RESULT_CODE = 110
        const val RESULT_KEY = "ProfilePicture"
    }
}



