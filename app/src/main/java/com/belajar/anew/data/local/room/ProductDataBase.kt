package com.belajar.anew.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.belajar.anew.data.local.entity.ProductFavoriteEntity


@Database(entities = [ProductFavoriteEntity::class], version = 2, exportSchema = false)

abstract class ProductDataBase: RoomDatabase() {
    abstract fun productDao(): ProductDao

    // object di dalam class
    companion object{
        @Volatile
        private var INSTANCE : ProductDataBase? = null
        fun getDatabase(context: Context): ProductDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDataBase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE=instance
                return instance
            }
        }
    }
}