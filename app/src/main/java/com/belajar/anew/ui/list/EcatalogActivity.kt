package com.belajar.anew.ui.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import com.belajar.MyCatalog.R
import com.belajar.MyCatalog.data.model.Product
import com.belajar.MyCatalog.data.remote.network.ApiConfig
import com.belajar.MyCatalog.data.remote.network.ApiService
import com.belajar.MyCatalog.data.repository.ProductRepository
import com.belajar.MyCatalog.databinding.ActivityEcatalogBinding
import com.belajar.MyCatalog.ui.detail.ProductDetailActivity
import com.belajar.MyCatalog.ui.list.ProductListAdapter
import com.belajar.MyCatalog.ui.list.ProductListAdapterListener
import com.belajar.MyCatalog.ui.list.ProductListViewModel
import com.belajar.MyCatalog.ui.list.ProductListViewModelFactory
import com.belajar.anew.data.local.room.ProductDao
import com.belajar.anew.data.preferences.UserPreferences
import com.belajar.anew.ui.Wishlist.WishlistActivity
import com.belajar.anew.ui.login.LoginActivity
import com.belajar.anew.ui.profile.ProfileActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//untuk membuat instance ke preferences dataStore
const val USER_PREFERENCES_NAME = "user_preferences"

//migrasi preferences dari sharedPreferences ke dataStore
val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME,
    produceMigrations = {context ->
        listOf(SharedPreferencesMigration(context, USER_PREFERENCES_NAME))
    })

class EcatalogActivity : AppCompatActivity () {

    //Mendeklarasikan variable binding untuk mereferensikan Binding
    //Mendeklarasikan variable binding untuk mereferensikan viewmodel
    private lateinit var binding: ActivityEcatalogBinding
    private lateinit var viewModel: ProductListViewModel

    //kondisi awal saat MainActivity baru diciptakan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEcatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(actionBar!=null){
            actionBar!!.setDisplayHomeAsUpEnabled(true);
            actionBar!!.setTitle("Profile")

        }


        ViewModelProvider(
            this,
            ProductListViewModelFactory(
                ProductRepository(ApiConfig.createService(ApiService::class.java), (ApiConfig.createService(ProductDao::class.java))), UserPreferences(dataStore, this)
            )
        )[ProductListViewModel::class.java].also { viewModel = it }



        // get products
        val items = viewModel.items
        binding.bindAdapter(items)

        //loginstatus dari viewmodel
        viewModel.userPreferencesFlow.observe(this){profile ->
            if(profile.loginstatus){

                Toast.makeText(this, profile.loginstatus.toString(), Toast.LENGTH_LONG).show()

            }else{
                val intent = Intent(this, LoginActivity::class.java).apply {
                }
                startActivity(intent)
                finish()
            }

        }

    }

    //untuk inisiasi fungsi logout
    private fun authLogout(viewModel: ProductListViewModel){
        viewModel.userLogin(false, "")

    }

    private fun ActivityEcatalogBinding.bindAdapter(items: Flow<PagingData<Product>>) {

        val adapter = ProductListAdapter(object : ProductListAdapterListener {
            override fun onClickProduct(ecatalog: Product) {
                startActivity(
                    Intent(this@EcatalogActivity, ProductDetailActivity::class.java)
                        .putExtra("EXTRA_ID", ecatalog.id)

                )
            }
        })
        binding.rvProducts.adapter = adapter
        binding.rvProducts.addItemDecoration(
            DividerItemDecoration(this@EcatalogActivity, DividerItemDecoration.VERTICAL)
        )
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                items.collect{
                    adapter.submitData(it)
                }
            }
        }
    }

    //untuk menentukan menu opsi untuk aktivitas
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_catalog, menu)
        return true
    }

    // untuk menentukan kejadian pada masing-masing opsi dengan mendapatkan id pada setiap menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorites -> {
                val intent = Intent(this, WishlistActivity::class.java).apply {
                }
                startActivity(intent)
                true
            }

            R.id.action_profile -> {
                //saat di klik pindah ke ProfileActivity
                val intent = Intent(this, ProfileActivity::class.java).apply {
                }
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                //saat di klik pindah ke LoginActivity
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show()


                authLogout(viewModel)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}