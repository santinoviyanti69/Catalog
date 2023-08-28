package com.belajar.anew.ui.login

import android.net.Uri
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

// untuk view ktivitas dan fragmen menyediakan metode untuk transaksi data
class LoginViewModel (
    private val repo: ProductRepository,
    private val userPreferences: UserPreferences
): ViewModel() {

    val userPreferencesFlow = userPreferences.userPreferencesFlow.asLiveData()
    fun userLogin(loginStatus: Boolean, email: String) {
        viewModelScope.launch {
            userPreferences.userLogin(loginStatus, email)
        }
    }
}

//untuk membuat instance viewmodel
class LoginViewModelFactory(
    private val repository: ProductRepository,
    private val userPreferencesRepository: UserPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}