package com.belajar.anew.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.belajar.MyCatalog.data.repository.ProductRepository
import com.belajar.anew.data.preferences.UserPreferences
import kotlinx.coroutines.launch

// untuk view ktivitas dan fragmen menyediakan metode untuk transaksi data
class ProfileViewModel(
    private val repository: ProductRepository,
    private val userPreferences: UserPreferences
): ViewModel() {
    val userPreferencesFlow = userPreferences.userPreferencesFlow.asLiveData()

}
//untuk membuat instance viewmodel
class ProfileViewModelFactory(
    private val repository: ProductRepository,
    private val userPreferencesRepository: UserPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}