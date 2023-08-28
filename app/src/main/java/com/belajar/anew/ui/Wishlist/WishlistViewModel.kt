package com.belajar.anew.ui.Wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.belajar.MyCatalog.data.model.Product
import com.belajar.MyCatalog.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class WishlistViewModel (private val repository: ProductRepository): ViewModel(){

    val itemsFavorite : Flow<PagingData<Product>> = repository
        .getFavoriteProducts()
        .cachedIn(viewModelScope)

}

class WishlistViewModelFactory(
    private val repository: ProductRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WishlistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}