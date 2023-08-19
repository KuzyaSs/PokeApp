package com.example.pokeapp.presentation.viewModel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokeapp.domain.useCase.search.GetFilteredPokemonListUseCase
import com.example.pokeapp.domain.useCase.search.GetPokemonListResultUseCase
import com.example.pokeapp.domain.useCase.search.IsLastPageUseCase

class SearchViewModelFactory(
    private val getPokemonListResultUseCase: GetPokemonListResultUseCase,
    private val getFilteredPokemonListUseCase: GetFilteredPokemonListUseCase,
    private val isLastPageUseCase: IsLastPageUseCase,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(
                getPokemonListResultUseCase = getPokemonListResultUseCase,
                getFilteredPokemonListUseCase = getFilteredPokemonListUseCase,
                isLastPageUseCase = isLastPageUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}