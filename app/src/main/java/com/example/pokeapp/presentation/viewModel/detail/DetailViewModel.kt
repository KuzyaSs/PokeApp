package com.example.pokeapp.presentation.viewModel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.domain.model.PokemonDetail
import com.example.pokeapp.domain.useCase.detail.DeletePokemonUseCase
import com.example.pokeapp.domain.useCase.detail.GetPokemonDetailByIdUseCase
import com.example.pokeapp.domain.useCase.detail.InsertPokemonUseCase
import com.example.pokeapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getPokemonDetailByIdUseCase: GetPokemonDetailByIdUseCase,
    private val insertPokemonUseCase: InsertPokemonUseCase,
    private val deletePokemonUseCase: DeletePokemonUseCase
) : ViewModel() {
    private val _pokemonDetail = MutableLiveData<Resource<PokemonDetail>>()
    val pokemonDetail: LiveData<Resource<PokemonDetail>> get() = _pokemonDetail

    fun setPokemonDetailById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonDetail.postValue(Resource.Loading(_pokemonDetail.value?.data))
            _pokemonDetail.postValue(getPokemonDetailByIdUseCase.execute(id = id))
        }
    }

    fun setIsFavourite(pokemon: Pokemon) {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonDetail.value?.data?.let { pokemonDetail ->
                if (pokemonDetail.isFavourite) {
                    deletePokemonUseCase.execute(pokemon)
                } else {
                    insertPokemonUseCase.execute(pokemon)
                }
                pokemonDetail.isFavourite = !pokemonDetail.isFavourite
                _pokemonDetail.postValue(Resource.Success(pokemonDetail))
            }
        }
    }
}