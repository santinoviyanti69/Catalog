package com.belajar.anew.ui.form

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.belajar.MyCatalog.data.repository.ProductRepository
import com.belajar.anew.data.preferences.UserPreferences
import kotlinx.coroutines.launch

// untuk view aktivitas dan fragmen menyediakan metode untuk transaksi data
class EditProfileViewModel(private val repository: ProductRepository,
                           private val userPreferences: UserPreferences
) : ViewModel(){
    val userPreferencesFlow = userPreferences.userPreferencesFlow.asLiveData()

    fun profile(nama: String, usia: String, alamat: String, pendidikan: String){
        viewModelScope.launch {
            userPreferences.profile(nama, usia, alamat, pendidikan)
        }
    }

    fun setImage(img: Uri?){
        viewModelScope.launch {
            userPreferences.setImage(img)
        }
    }

}

//untuk membuat instance viewmodel
class EditProfileViewModelFactory(
    private val repository: ProductRepository,
    private val userPreferencesRepository: UserPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditProfileViewModel(repository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}