package com.belajar.MyCatalog.data.remote.network

import com.belajar.MyCatalog.data.remote.response.ProductResponse
import com.belajar.MyCatalog.data.remote.response.ProductsItemResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//untuk mengambil data product dan id product dari API
interface ApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("size") size: Int,
        @Query("skip") skip: Int
    ): ProductResponse

    @GET("product/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): ProductsItemResponse

}