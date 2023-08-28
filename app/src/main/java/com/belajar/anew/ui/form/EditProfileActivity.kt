package com.belajar.anew.ui.form

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.belajar.MyCatalog.R
import com.belajar.MyCatalog.data.remote.network.ApiConfig
import com.belajar.MyCatalog.data.remote.network.ApiService
import com.belajar.MyCatalog.data.repository.ProductRepository
import com.belajar.MyCatalog.databinding.ActivityEditProfileBinding
import com.belajar.anew.data.local.room.ProductDao
import com.belajar.anew.data.preferences.UserPreferences
import com.belajar.anew.ui.camera.Camera
import com.belajar.anew.ui.camera.Camera.Companion.RESULT_KEY
import com.belajar.anew.ui.list.dataStore
import com.belajar.anew.ui.profile.ProfileActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditProfileActivity : AppCompatActivity() {

    //Mendeklarasikan variable binding untuk mereferensikan Binding
    //Mendeklarasikan variable binding untuk mereferensikan viewmodel
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var Image: FloatingActionButton

    private val requestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if(result.resultCode == Camera.RESULT_CODE && result.data != null){
            val data = result.data?.getStringExtra(RESULT_KEY)
            val imgUri = Uri.parse(data)

            setImage(imgUri)
        }

    }
    //kondisi awal saat MainActivity baru diciptakan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.editprofile)


        Image = binding.Image
        Image.setOnClickListener {


            val listPicker = listOf<String>("Galery", "Camera")
            // untuk menampilkan list berbentuk dialog
            MaterialDialog(this)
                .title(R.string.pilih_gambar)
                .listItems(items = listPicker) { dialog, index, text ->
                    when (index){
                        0 -> {
                            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                            changeImage.launch(pickImg)
                        }
                        1 -> {
                            val intent = Intent(this, Camera::class.java)
                            requestLauncher.launch(intent)
                        }

                    }
                }
                .show()
        }



        ViewModelProvider(
            this,
            EditProfileViewModelFactory(
                ProductRepository(ApiConfig.createService(ApiService::class.java),(ApiConfig.createService(ProductDao::class.java))), UserPreferences(dataStore, this)
            )
        )[EditProfileViewModel::class.java].also { viewModel = it }

        viewModel.userPreferencesFlow.observe(this) {
            binding.edtnama.setText(it.nama)
            binding.edtusia.setText(it.usia)
            binding.edtalamat.setText(it.alamat)
            binding.edtpendidikan.setText(it.pendidikan)
            Glide.with(this).load(it.imgProfile)
                .transform(CenterInside())
                .into(binding.imgProfile)

        }



        binding.btnSave.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {

                startActivity(this)
            }


            profile()
            Toast.makeText(this, "Profile save", Toast.LENGTH_LONG).show()

        }
    }

    //untuk inisiasi fungsi dari data profile
    private fun profile() {
        viewModel.profile(
            nama = binding.edtnama.text.toString(),
            usia = binding.edtusia.text.toString(),
            alamat = binding.edtalamat.text.toString(),
            pendidikan = binding.edtpendidikan.text.toString()
        )
    }
    //untuk inisiasi fungsi image

    private fun setImage(img: Uri?){
        viewModel.setImage(img)
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                setImage(imgUri)
            }
        }


    //object dalam class
    companion object{

        const val TAG = "CameraXApp"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 10

        val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}



