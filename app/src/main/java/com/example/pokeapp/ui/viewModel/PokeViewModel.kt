package com.example.pokeapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.data.database.model.Pokemon
import com.example.pokeapp.data.remote.model.PokemonResponse
import com.example.pokeapp.data.repository.PokeRepository
import com.example.pokeapp.util.Constants.Companion.BASE_IMAGE_URL
import com.example.pokeapp.util.Constants.Companion.CONVERSION_ERROR_MESSAGE
import com.example.pokeapp.util.Constants.Companion.DEFAULT_RESPONSE_LIMIT
import com.example.pokeapp.util.Constants.Companion.DEFAULT_RESPONSE_OFFSET
import com.example.pokeapp.util.Constants.Companion.EMPTY_ERROR_MESSAGE
import com.example.pokeapp.util.Constants.Companion.NETWORK_ERROR_MESSAGE
import com.example.pokeapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class PokeViewModel(private val pokeRepository: PokeRepository) : ViewModel() {
    private var _pokemonResponse: PokemonResponse? = null

    private val _pokemonList = MutableLiveData<Resource<MutableList<Pokemon>>>()
    val pokemonList: LiveData<Resource<MutableList<Pokemon>>> get() = _pokemonList

    private var _bottomNavigationViewVisibility = MutableLiveData(true)
    val bottomNavigationViewVisibility: LiveData<Boolean> get() = _bottomNavigationViewVisibility

    init {
        getPokemonList()
    }

    fun getPokemonList() {
        if (_pokemonList.value !is Resource.Loading) {
            viewModelScope.launch(Dispatchers.IO) {
                _pokemonList.postValue(Resource.Loading(_pokemonList.value?.data))
                try {
                    val pokemonResponse = pokeRepository.getPokemonList(
                        getLimitFromUrl(_pokemonResponse?.next),
                        getOffsetFromUrl(_pokemonResponse?.next)
                    )
                    _pokemonList.postValue(handlePokemonResponse(pokemonResponse))
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is IOException -> _pokemonList.postValue(
                            Resource.Error(
                                _pokemonList.value?.data,
                                message = NETWORK_ERROR_MESSAGE
                            )
                        )

                        else -> _pokemonList.postValue(
                            Resource.Error(
                                _pokemonList.value?.data,
                                message = CONVERSION_ERROR_MESSAGE
                            )
                        )
                    }
                }
            }
        }
    }

    fun isLastPage(): Boolean {
        if (_pokemonResponse?.previous?.isNotEmpty() == true && _pokemonResponse?.next.isNullOrEmpty()) {
            return true
        }
        return false
    }

    fun resetErrorMessage() {
        when(_pokemonList.value) {
            is Resource.Error -> {
                if (_pokemonList.value?.data?.isNotEmpty() == true) {
                    _pokemonList.postValue(
                        Resource.Error(
                            _pokemonList.value?.data,
                            EMPTY_ERROR_MESSAGE
                        )
                    )
                }
            }
            else -> { }
        }
    }

    fun setBottomNavigationViewVisibility(isVisible: Boolean) {
        _bottomNavigationViewVisibility.postValue(isVisible)
    }

    private fun handlePokemonResponse(response: Response<PokemonResponse>): Resource<MutableList<Pokemon>> {
        if (response.isSuccessful) {
            response.body()?.let { pokemonResponse ->
                _pokemonResponse = pokemonResponse

                val pokemonList: List<Pokemon> = pokemonResponse.results.map { pokemon ->
                    val pokemonId = pokemon.url.dropLast(1).takeLastWhile { char ->
                        char.isDigit()
                    }.toInt()

                    Pokemon(
                        pokemonId,
                        pokemon.name,
                        BASE_IMAGE_URL.plus("$pokemonId.png")
                    )
                }

                _pokemonList.value?.data?.let { oldPokemonList ->
                    oldPokemonList.addAll(pokemonList)
                    return Resource.Success(oldPokemonList)
                }
                return Resource.Success(pokemonList.toMutableList())
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
        }?.drop(1)?.takeWhile { char ->
            char.isDigit()
        }?.toInt() ?: DEFAULT_RESPONSE_OFFSET
    }
}

class PokeViewModelFactory(private val pokeRepository: PokeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokeViewModel(pokeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}