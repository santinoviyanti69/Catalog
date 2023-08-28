package com.belajar.anew.data.mapper

import android.icu.text.NumberFormat
import com.belajar.MyCatalog.data.model.Product
import com.belajar.anew.data.local.entity.ProductFavoriteEntity
import java.util.Locale

// fun sebagai fungsi elemen penyusun
// untuk mapping dataclass dari ProductFavoriteEntity
fun ProductFavoriteEntity.toModel() =
    Product(
        discountPercentage = this.discountPercentage,
        thumbnail = this.thumbnail,
        images = this.images.split(","),
        price = this.price,
        formattedPrice = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this.price),
        rating = (this.rating / 5 * 100),
        description = this.description,
        id = this.id,
        title = this.title,
        stock = this.stock,
        category = this.category,
        brand = this.brand,
        isFavorite = true
    )

