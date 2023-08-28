package com.belajar.MyCatalog.ui.detail

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.belajar.MyCatalog.R
import com.belajar.MyCatalog.data.remote.network.ApiConfig
import com.belajar.MyCatalog.data.remote.network.ApiService
import com.belajar.MyCatalog.data.repository.ProductRepository
import com.belajar.MyCatalog.databinding.ActivityProductDetailBinding
import com.belajar.anew.data.local.room.ProductDataBase
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class ProductDetailActivity : AppCompatActivity() {

    //Mendeklarasikan variable binding untuk mereferensikan Binding
    //Mendeklarasikan variable binding untuk mereferensikan database
    //Mendeklarasikan variable binding untuk mereferensikan viewmodel
    private lateinit var binding: ActivityProductDetailBinding
    private val dataBase : ProductDataBase by lazy {
        ProductDataBase.getDatabase(this)
    }

    private val viewModelDetail: ProductViewModelDetail by viewModels{
        DetailProductViewModelFactory(ProductRepository(ApiConfig.createService(ApiService::class.java), dataBase.productDao()
        ))
    }

    //kondisi awal saat MainActivity baru diciptakan
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.detailproduct)

        //intent sebagai penghubung interaksi antar Activity di aplikasi Android
        val id = intent.getIntExtra("EXTRA_ID", 0)
        viewModelDetail.getDetailProduct(id)

        viewModelDetail.detailProduct.observe(this){

            Glide.with(this)
                .load(it.images[0])
                .placeholder(R.drawable.baseline_downloading_24)
                .transform(CenterInside(), RoundedCorners(24))
                .into(binding.productImageDetail)


            binding.productName.text = it.title
            binding.productPrice.text = it.formattedPrice
            binding.ratingBar.rating = it.rating.toFloat()
            binding.Description.text = it.description

            Log.d("DetailProduct", it.isFavorite.toString())

            if(it.isFavorite){
                binding.btnFavorite.changeIconColor(R.color.md_theme_light_primary)
            }else{
                binding.btnFavorite.changeIconColor(R.color.grey_500)
            }

        }
        binding.btnFavorite.setOnClickListener {
            Log.d("buttonFavorite", "sudah diklik")
            viewModelDetail.setFavorite()
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

fun ImageButton.changeIconColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}



