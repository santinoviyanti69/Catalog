package com.belajar.anew.ui.Wishlist

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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
import com.belajar.MyCatalog.databinding.ActivityWishlistBinding
import com.belajar.MyCatalog.ui.detail.ProductDetailActivity
import com.belajar.MyCatalog.ui.list.ProductListAdapter
import com.belajar.MyCatalog.ui.list.ProductListAdapterListener
import com.belajar.anew.data.local.room.ProductDataBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class WishlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWishlistBinding
    private lateinit var viewModel: WishlistViewModel
    private val database : ProductDataBase by lazy {
        ProductDataBase.getDatabase(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.wishlist)


        ViewModelProvider(
            this,
            WishlistViewModelFactory(
                ProductRepository(
                    ApiConfig.createService(ApiService::class.java),database.productDao()),
                )
            )[WishlistViewModel::class.java].also { viewModel = it}
        // untuk mengambil product item
        val items = viewModel.itemsFavorite
        binding.bindAdapter(items)
    }
    private fun ActivityWishlistBinding.bindAdapter(items: Flow<PagingData<Product>>) {
        val adapter = ProductListAdapter(object : ProductListAdapterListener {
            override fun onClickProduct(product: Product) {
                startActivity(
                    Intent(this@WishlistActivity, ProductDetailActivity::class.java)
                        .putExtra("EXTRA_ID", product.id)
                )
            }
        })
        binding.Favorite.adapter = adapter
        binding.Favorite.addItemDecoration(
            DividerItemDecoration(this@WishlistActivity, DividerItemDecoration.VERTICAL)
        )
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                items.collect {
                    adapter.submitData(it)
                }
            }
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
