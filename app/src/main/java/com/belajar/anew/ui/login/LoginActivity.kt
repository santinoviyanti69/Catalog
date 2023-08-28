package com.belajar.anew.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.belajar.anew.ui.list.EcatalogActivity
import com.belajar.MyCatalog.data.remote.network.ApiConfig
import com.belajar.MyCatalog.data.remote.network.ApiService
import com.belajar.MyCatalog.data.repository.ProductRepository
import com.belajar.MyCatalog.databinding.ActivityLoginBinding
import com.belajar.anew.data.local.room.ProductDao
import com.belajar.anew.data.preferences.UserPreferences
import com.belajar.anew.ui.list.dataStore


class LoginActivity : AppCompatActivity() {

    //Mendeklarasikan variable binding untuk mereferensikan Binding
    //Mendeklarasikan variable binding untuk mereferensikan viewmodel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    //kondisi awal saat MainActivity baru diciptakan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        ViewModelProvider(
            this,
            LoginViewModelFactory(
                ProductRepository(ApiConfig.createService(ApiService::class.java),(ApiConfig.createService(ProductDao::class.java))),
                UserPreferences(dataStore, this
                )
            )
        )[LoginViewModel::class.java].also { viewModel = it }


        viewModel.userPreferencesFlow.observe(this){profile ->

            if (profile.loginstatus){

                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_LONG).show()
                val intent = Intent(this, EcatalogActivity::class.java).apply {
                }
                startActivity(intent)
                finish()

            }

        }

        binding.loginbutton.setOnClickListener{


            val email = binding.Email.text.toString()
            val password = binding.Password.text.toString()


            if (email.isEmpty() ){
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_LONG).show()
            }else if (password.isEmpty()){
                Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_LONG).show()
            } else{

                userLogin()
                Toast.makeText(this, "email save", Toast.LENGTH_LONG).show()



            }
        }
    }


    //untuk menginisiasi fungsi dari data userLogin
    private fun userLogin(){
        viewModel.userLogin(true, email = binding.Email.text.toString())
    }


}



