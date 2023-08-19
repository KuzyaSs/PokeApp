package com.example.pokeapp.presentation.viewModel.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.domain.useCase.favourite.GetFilteredFavouritePokemonListUseCase
import com.example.pokeapp.domain.useCase.favourite.GetFavouritePokemonListUseCase

class FavouriteViewModel(
    getFavouritePokemonListUseCase: GetFavouritePokemonListUseCase,
    private val getFilteredFavouritePokemonListUseCase: GetFilteredFavouritePokemonListUseCase
) : ViewModel() {
    private var _favouritePokemonList: LiveData<List<Pokemon>> =
        getFavouritePokemonListUseCase.execute().asLiveData()
    val favouritePokemonList: LiveData<List<Pokemon>> get() = _favouritePokemonList

    fun getFilteredFavouritePokemonList(searchString: String): List<Pokemon> {
        return getFilteredFavouritePokemonListUseCase.execute(
            pokemonList = _favouritePokemonList.value ?: listOf(),
            searchString = searchString
        )
    }
}