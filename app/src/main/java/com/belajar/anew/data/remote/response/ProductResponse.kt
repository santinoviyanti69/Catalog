package com.belajar.MyCatalog.data.remote.response

import com.google.gson.annotations.SerializedName

//untuk menyimpan data item dari ProductsResponse
data class ProductResponse(

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("limit")
    val limit: Int,

    @field:SerializedName("skip")
    val skip: Int,

    @field:SerializedName("products")
    val products: List<ProductsItemResponse>

)

//untuk menyimpan data item dari ProductsItemResponse
data class ProductsItemResponse(

    @field:SerializedName("discountPercentage")
    val discountPercentage: Double,

    @field:SerializedName("thumbnail")
    val thumbnail: String,

    @field:SerializedName("images")
    val images: List<String>,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("rating")
    val rating: Double,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("stock")
    val stock: Int,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("brand")
    val brand: String

)
