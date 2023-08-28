package com.belajar.MyCatalog.data.mapper

import com.belajar.MyCatalog.data.model.Product
import com.belajar.MyCatalog.data.remote.response.ProductResponse
import com.belajar.MyCatalog.data.remote.response.ProductsItemResponse
import java.text.NumberFormat
import java.util.Locale

// fun sebagai fungsi elemen penyusun
// untuk mapping dataclass dari ProductsResponse
fun ProductResponse.toModel(): List<Product>{
    return this.products.map {
        it.toModel()
    }
}

// fun sebagai fungsi elemen penyusun
// untuk mapping dataclass dari ProductsItemResponse
fun ProductsItemResponse.toModel() =
    Product(
        discountPercentage,
        thumbnail,
        images,
        price,
        rating,
        description,
        id,
        title,
        stock,
        category,
        brand,
        formattedPrice = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this.price),
    )
