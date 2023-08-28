package com.belajar.MyCatalog.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.belajar.MyCatalog.data.mapper.toModel
import com.belajar.MyCatalog.data.model.Product
import com.belajar.MyCatalog.data.paging.ProductsPagingSource
import com.belajar.MyCatalog.data.paging.SIZE
import com.belajar.MyCatalog.data.remote.network.ApiService
import com.belajar.anew.data.local.room.ProductDao
import com.belajar.anew.data.mapper.toModel
import com.belajar.anew.data.mapper.toProductFavoriteEntity
import com.belajar.anew.data.paging.WishListPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepository(
    private val service: ApiService,
    private val productDao: ProductDao
){

    fun gerFavorite(id: Int): Flow<Product?>{
        return productDao.getProductById(id).map {
            it?.toModel()
        }
    }

    suspend fun setFavoriteDelete(product: Product) {
        return productDao.delete(product.toProductFavoriteEntity())
    }

    suspend fun setFavoriteInsert(product: Product) {
        return productDao.insert(product.toProductFavoriteEntity())
    }
    suspend fun getProduct(id: Int): Product{
        return service.getProduct(id).toModel()
    }

    fun getFavoriteProducts(): Flow<PagingData<Product>>{
        return Pager(
            config = PagingConfig(pageSize = SIZE, enablePlaceholders = false),
            pagingSourceFactory = { WishListPagingSource(productDao) }
        )
            .flow
    }

    //untuk menangani cache dalam memori dan meminta data saat user ada di akhir page
    fun getProducts(): Flow<PagingData<Product>> {
        return Pager (
            config = PagingConfig(pageSize = SIZE, enablePlaceholders = false),
            pagingSourceFactory = {ProductsPagingSource(service)}
                )
            .flow
    }
}