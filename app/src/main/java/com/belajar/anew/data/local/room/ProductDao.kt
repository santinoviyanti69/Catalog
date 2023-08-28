package com.belajar.anew.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.belajar.anew.data.local.entity.ProductFavoriteEntity
import com.belajar.anew.data.local.entity.TABLE_PRODUCTS
import kotlinx.coroutines.flow.Flow

// sebagai Objek Akses Data untuk menentukan interaksi database
@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg item: ProductFavoriteEntity)

    @Query("SELECT * FROM $TABLE_PRODUCTS LIMIT :limit OFFSET :skip")
    fun loadAll(skip: Int , limit: Int): List<ProductFavoriteEntity>

    @Query("SELECT * FROM $TABLE_PRODUCTS WHERE id= :id")
    fun getProductById(id: Int): Flow<ProductFavoriteEntity?>

//    @Query("SELECT * FROM $TABLE_PRODUCTS")
//    fun loadAll(): LiveData<List<ProductFavoriteEntity>>
//
//    @Query("SELECT * FROM $TABLE_PRODUCTS WHERE id= :id")
//    fun getProductById(id: Int): Flow<ProductFavoriteEntity?>

    @Delete
    suspend fun delete(vararg item: ProductFavoriteEntity)
}