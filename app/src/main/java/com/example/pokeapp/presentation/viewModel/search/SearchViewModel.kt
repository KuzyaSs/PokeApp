package com.example.pokeapp.presentation.viewModel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.domain.model.PokemonListResult
import com.example.pokeapp.domain.useCase.search.GetFilteredPokemonListUseCase
import com.example.pokeapp.domain.useCase.search.GetPokemonListResultUseCase
import com.example.pokeapp.domain.useCase.search.IsLastPageUseCase
import com.example.pokeapp.util.Constants.Companion.EMPTY_STRING
import com.example.pokeapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getPokemonListResultUseCase: GetPokemonListResultUseCase,
    private val getFilteredPokemonListUseCase: GetFilteredPokemonListUseCase,
    private val isLastPageUseCase: IsLastPageUseCase,
) : ViewModel() {
    private val _pokemonListResult = MutableLiveData<Resource<PokemonListResult>>()
    val pokemonListResult: LiveData<Resource<PokemonListResult>> get() = _pokemonListResult

    init {
        setPokemonList()
    }

    fun setPokemonList() {
        viewModelScope.launch(Dispatchers.IO) {
            val pokemonListResult = _pokemonListResult.value?.data ?: PokemonListResult(
                nextUrl = EMPTY_STRING,
                previousUrl = EMPTY_STRING,
                pokemonList = listOf()
            )
            _pokemonListResult.postValue(Resource.Loading(pokemonListResult))
            _pokemonListResult.postValue(getPokemonListResultUseCase.execute(pokemonListResult))
        }
    }

    fun getFilteredPokemonList(searchString: String): List<Pokemon> {
        return getFilteredPokemonListUseCase.execute(
            pokemonList = pokemonListResult.value?.data?.pokemonList ?: listOf(),
            searchString = searchString,
            errorMessage = pokemonListResult.value?.message ?: EMPTY_STRING
        )
    }

    fun isLastPokemonListPage(): Boolean {
        val pokemonListResult = _pokemonListResult.value?.data ?: PokemonListResult(
            nextUrl = EMPTY_STRING,
            previousUrl = EMPTY_STRING,
            pokemonList = listOf()
        )
        return isLastPageUseCase.execute(pokemonListResult)
    }
}