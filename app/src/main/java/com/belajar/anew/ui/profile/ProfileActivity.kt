package com.belajar.anew.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.belajar.MyCatalog.R
import com.belajar.MyCatalog.data.remote.network.ApiConfig
import com.belajar.MyCatalog.data.remote.network.ApiService
import com.belajar.MyCatalog.data.repository.ProductRepository
import com.belajar.MyCatalog.databinding.ActivityProfileBinding
import com.belajar.anew.data.local.room.ProductDao
import com.belajar.anew.data.preferences.UserPreferences
import com.belajar.anew.ui.form.EditProfileActivity
import com.belajar.anew.ui.list.dataStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside

class ProfileActivity : AppCompatActivity() {

    //Mendeklarasikan variable binding untuk mereferensikan Binding
    //Mendeklarasikan variable binding untuk mereferensikan viewmodel
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    //kondisi awal saat MainActivity baru diciptakan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.userprofile)


        ViewModelProvider(
            this,
            ProfileViewModelFactory(
                ProductRepository(
                    ApiConfig.createService(ApiService::class.java),
                    (ApiConfig.createService(ProductDao::class.java))
                ), UserPreferences(dataStore, this)
            )
        )[ProfileViewModel::class.java].also { viewModel = it }

        viewModel.userPreferencesFlow.observe(this) { profile ->


            binding.Emailpf.text = profile.email
            binding.Namapf.text = profile.nama
            binding.Usiapf.text = profile.usia
            binding.Alamatpf.text = profile.alamat
            binding.Pendidikanpf.text = profile.pendidikan
            Glide.with(this).load(profile.imgProfile)
                .transform(CenterInside())
                .into(binding.imgProfile)


        }


        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)

    }



}