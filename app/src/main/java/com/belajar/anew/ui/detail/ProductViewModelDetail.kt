package com.belajar.MyCatalog.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.belajar.MyCatalog.data.model.Product
import com.belajar.MyCatalog.data.remote.network.ApiConfig
import com.belajar.MyCatalog.data.remote.network.ApiService
import com.belajar.MyCatalog.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModelDetail(private val repo: ProductRepository
): ViewModel() {

    val resultSuccessFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()
    private val _detailProduct = MutableLiveData<Product>()
    val detailProduct: LiveData<Product>
        get() = _detailProduct
    fun getDetailProduct(id: Int) {
        viewModelScope.launch {
            val detailProduct = repo.getProduct(id)
            setProduct(product = detailProduct)
        }
    }

    fun setProduct(product: Product){
        viewModelScope.launch {
            repo.gerFavorite(product.id).collect{
                if(it == null){
                    _detailProduct.postValue(product)

                }else{
                    _detailProduct.postValue(it)
                }
            }
        }
    }

    fun setFavorite() {
        viewModelScope.launch {
            if(_detailProduct.value != null){
                if (_detailProduct.value?.isFavorite == true){
                    repo.setFavoriteDelete(_detailProduct.value!!)
                }else{
                    repo.setFavoriteInsert(_detailProduct.value!!)
                }
            }
        }
    }

}

class DetailProductViewModelFactory(
    private val repository: ProductRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModelDetail::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModelDetail(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}