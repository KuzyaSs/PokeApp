package com.example.pokeapp.presentation.viewModel.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    private var _bottomNavigationViewVisibility = MutableLiveData(true)
    val bottomNavigationViewVisibility: LiveData<Boolean> get() = _bottomNavigationViewVisibility

    fun setBottomNavigationViewVisibility(isVisible: Boolean) {
        _bottomNavigationViewVisibility.postValue(isVisible)
    }
}