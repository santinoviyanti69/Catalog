package com.belajar.MyCatalog.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.belajar.MyCatalog.data.model.Product
import com.belajar.MyCatalog.data.repository.ProductRepository
import com.belajar.anew.data.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductListViewModel(
    // untuk view ktivitas dan fragmen menyediakan metode untuk transaksi data
    private val repo: ProductRepository,
    private val userPreferences: UserPreferences

): ViewModel() {

    val items : Flow<PagingData<Product>> = repo
        .getProducts()
        .cachedIn(viewModelScope)

    val userPreferencesFlow = userPreferences.userPreferencesFlow.asLiveData()
    fun userLogin(loginStatus: Boolean, email: String) {
        viewModelScope.launch {
            userPreferences.userLogin(loginStatus, email)
        }
    }

}
//untuk membuat instance viewmodel
class ProductListViewModelFactory(
    private val repository: ProductRepository,
    private val userPreferencesRepository: UserPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductListViewModel(repository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}