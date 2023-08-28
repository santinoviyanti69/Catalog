package com.belajar.MyCatalog.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.belajar.MyCatalog.data.mapper.toModel
import com.belajar.MyCatalog.data.model.Product
import com.belajar.MyCatalog.data.remote.network.ApiService

// ketika ingin berbagi nilai antar class di dalam file
const val STARTING_KEY = 0
const val SIZE = 30
const val SKIP = 30

// class paging source sebagai sumber data dan cara mengambil data dari sumber tersebut
class ProductsPagingSource (

    // private val agar tidak dapat diubah oleh class lain
    private val ApiService : ApiService
): PagingSource<Int, Product>() {

    // ovveride fun proses penulisan ulang fungsi yang ada di Parent Class dari dalam Child class
    // getRefreshkey untuk merefresh data pada aplikasi
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)

        }
    }

    //untuk mengambil lebih banyak data yang akan ditampilkan saat user melakukan scroll secara asinkron
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val pageNumber = params.key ?: STARTING_KEY
        val items = ApiService.getProducts(SIZE, SKIP).toModel()

        return try {
            LoadResult.Page(
                data = items,
                prevKey = if (pageNumber > 0) pageNumber - 1 else null,
                nextKey = pageNumber.plus(1)
            )
        //memanggil tindakan tertentu dengan pengecualian yang tertangkap
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
