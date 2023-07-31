package com.example.pokeapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.data.database.model.Pokemon
import com.example.pokeapp.data.remote.model.PokemonResponse
import com.example.pokeapp.data.repository.PokeRepository
import com.example.pokeapp.util.Constants.Companion.DEFAULT_RESPONSE_LIMIT
import com.example.pokeapp.util.Constants.Companion.DEFAULT_RESPONSE_OFFSET
import com.example.pokeapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class PokeViewModel(private val pokeRepository: PokeRepository) : ViewModel() {
    private var _pokemonResponse: PokemonResponse? = null

    private val _pokemonList = MutableLiveData<Resource<List<Pokemon>>>()
    val pokemonList: LiveData<Resource<List<Pokemon>>> get() = _pokemonList

    fun getPokemonList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pokemonResponse = pokeRepository.getPokemonList(
                    getLimitFromUrl(_pokemonResponse?.next),
                    getOffsetFromUrl(_pokemonResponse?.next)
                )
                _pokemonList.value = handlePokemonResponse(pokemonResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _pokemonList.value =
                        Resource.Error(message = "Network failure")

                    else -> _pokemonList.value = Resource.Error(message = "Conversion error")
                }
            }
        }
    }

    private fun handlePokemonResponse(response: Response<PokemonResponse>): Resource<List<Pokemon>> {
        if (response.isSuccessful) {
            response.body()?.let { pokemonResponse ->
                _pokemonResponse = pokemonResponse
                val pokemonList: List<Pokemon> = pokemonResponse.results.map { pokemon ->
                    Pokemon(
                        pokemon.url.dropLast(1).takeLastWhile { it.isDigit() }.toInt(),
                        pokemon.name,
                        pokemon.url // TODO *via link!!!!!!!!!!
                    )
                }
                return Resource.Success(pokemonList)
            }
        }
        return Resource.Error(message = response.message())
    }

    private fun getLimitFromUrl(url: String?): Int {
        return url?.takeLastWhile { char ->
            char.isDigit()
        }?.toInt() ?: DEFAULT_RESPONSE_LIMIT
    }

    private fun getOffsetFromUrl(url: String?): Int {
        return url?.dropWhile { char ->
            char != '='
        }?.takeWhile { char ->
            char.isDigit()
        }?.toInt() ?: DEFAULT_RESPONSE_OFFSET
    }
}