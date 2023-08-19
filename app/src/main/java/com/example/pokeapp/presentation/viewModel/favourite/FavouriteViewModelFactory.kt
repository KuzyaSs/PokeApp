package com.example.pokeapp.presentation.viewModel.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokeapp.domain.useCase.favourite.GetFilteredFavouritePokemonListUseCase
import com.example.pokeapp.domain.useCase.favourite.GetFavouritePokemonListUseCase

class FavouriteViewModelFactory(
    private val getFavouritePokemonListUseCase: GetFavouritePokemonListUseCase,
    private val getFilteredFavouritePokemonListUseCase: GetFilteredFavouritePokemonListUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavouriteViewModel(
                getFavouritePokemonListUseCase = getFavouritePokemonListUseCase,
                getFilteredFavouritePokemonListUseCase = getFilteredFavouritePokemonListUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}