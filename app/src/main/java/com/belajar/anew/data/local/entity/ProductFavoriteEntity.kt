package com.belajar.anew.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_PRODUCTS = "product_favorite"
@Entity(tableName = TABLE_PRODUCTS)

data class ProductFavoriteEntity(

    @ColumnInfo(name = "images")
    val images: String,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name = "rating")
    val rating: Double,
    @ColumnInfo(name = "formatted_price")
    val formattedPrice: String,
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name ="stock" )
    val stock: Int,
    @ColumnInfo(name ="category" )
    val category: String,
    @ColumnInfo(name ="brand" )
    val brand: String,
    @ColumnInfo(name ="description" )
    val description : String,
    @ColumnInfo(name ="thumbnail" )
    val thumbnail :String,
    @ColumnInfo(name ="discount_percentage" )
    val discountPercentage :Double,
)
