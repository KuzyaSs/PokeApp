package com.example.pokeapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.data.database.model.Pokemon
import com.example.pokeapp.data.remote.model.PokemonDetail
import com.example.pokeapp.data.remote.model.PokemonListResponse
import com.example.pokeapp.repository.PokeRepository
import com.example.pokeapp.util.Constants.Companion.BASE_IMAGE_URL
import com.example.pokeapp.util.Constants.Companion.CONVERSION_ERROR_MESSAGE
import com.example.pokeapp.util.Constants.Companion.DEFAULT_RESPONSE_LIMIT
import com.example.pokeapp.util.Constants.Companion.DEFAULT_RESPONSE_OFFSET
import com.example.pokeapp.util.Constants.Companion.ERROR_POKEMON_ID
import com.example.pokeapp.util.Constants.Companion.NETWORK_ERROR_MESSAGE
import com.example.pokeapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class PokeViewModel(private val pokeRepository: PokeRepository) : ViewModel() {
    private var _pokemonListResponse: PokemonListResponse? = null

    private val _pokemonList = MutableLiveData<Resource<MutableList<Pokemon>>>()
    val pokemonList: LiveData<Resource<MutableList<Pokemon>>> get() = _pokemonList

    private var _favouritePokemonList: LiveData<List<Pokemon>> = pokeRepository.getFavouritePokemonList()
    val favouritePokemonList: LiveData<List<Pokemon>> get() = _favouritePokemonList

    private val _pokemonDetail = MutableLiveData<Resource<PokemonDetail>>()
    val pokemonDetail: LiveData<Resource<PokemonDetail>> get() = _pokemonDetail

    private var _bottomNavigationViewVisibility = MutableLiveData(true)
    val bottomNavigationViewVisibility: LiveData<Boolean> get() = _bottomNavigationViewVisibility

    init {
        setPokemonList()
    }

    fun getPokemonList(searchString: String): MutableList<Pokemon> {
        val newPokemonList = mutableListOf<Pokemon>()
        _pokemonList.value?.data?.let { pokemonList ->
            if (searchString.isBlank()) {
                newPokemonList.addAll(pokemonList)
            } else {
                newPokemonList.addAll(pokemonList.filter { pokemon ->
                    pokemon.name.startsWith(searchString)
                })
                return newPokemonList
            }
        }
        if (searchString.isBlank()) {
            _pokemonList.value?.message?.let { errorMessage ->
                for (i in 0 until DEFAULT_RESPONSE_LIMIT) {
                    val errorPokemon = Pokemon(ERROR_POKEMON_ID, errorMessage, errorMessage)
                    newPokemonList.add(errorPokemon)
                }
            }
        }
        return newPokemonList
    }

    fun getFavouritePokemonList(searchString: String): List<Pokemon> {
        val newPokemonList = mutableListOf<Pokemon>()
        _favouritePokemonList.value?.let { pokemonList ->
            if (searchString.isBlank()) {
                return pokemonList
            }
            newPokemonList.addAll(pokemonList.filter { pokemon ->
                pokemon.name.startsWith(searchString)
            })
        }
        return newPokemonList
    }

    fun setPokemonList() {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList.postValue(Resource.Loading(_pokemonList.value?.data))
            try {
                val pokemonListResponse = pokeRepository.getPokemonList(
                    getLimitFromUrl(_pokemonListResponse?.next),
                    getOffsetFromUrl(_pokemonListResponse?.next)
                )
                _pokemonList.postValue(handlePokemonListResponse(pokemonListResponse))
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _pokemonList.postValue(
                        Resource.Error(
                            _pokemonList.value?.data,
                            NETWORK_ERROR_MESSAGE
                        )
                    )
                    else -> _pokemonList.postValue(
                        Resource.Error(
                            _pokemonList.value?.data,
                            CONVERSION_ERROR_MESSAGE
                        )
                    )
                }
            }
        }
    }

    fun setPokemonDetail(pokemonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonDetail.postValue(Resource.Loading(_pokemonDetail.value?.data))
            try {
                val pokemonDetailResponse = pokeRepository.getPokemonDetail(pokemonName)
                _pokemonDetail.postValue(handlePokemonDetailResponse(pokemonDetailResponse))
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _pokemonDetail.postValue(
                        Resource.Error(
                            _pokemonDetail.value?.data,
                            NETWORK_ERROR_MESSAGE
                        )
                    )
                    else -> _pokemonDetail.postValue(
                        Resource.Error(
                            _pokemonDetail.value?.data,
                            CONVERSION_ERROR_MESSAGE
                        )
                    )
                }
            }
        }
    }

    fun setIsFavourite(pokemon: Pokemon) {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonDetail.value?.data?.let { pokemonDetail ->
                if (pokemonDetail.isFavourite) {
                    pokeRepository.deletePokemon(pokemon)
                } else {
                    pokeRepository.insertPokemon(pokemon)
                }
                pokemonDetail.isFavourite = pokeRepository.isFavouritePokemonById(pokemonDetail.id)
                _pokemonDetail.postValue(Resource.Success(pokemonDetail))
            }
        }
    }

    fun isLastPokemonListPage(): Boolean {
        _pokemonListResponse?.let { pokemonListResponse ->
            pokemonListResponse.previous?.let { previous ->
                if (previous.isNotEmpty() && pokemonListResponse.next.isNullOrEmpty()) {
                    return true
                }
            }
        }
        return false
    }

    fun setBottomNavigationViewVisibility(isVisible: Boolean) {
        _bottomNavigationViewVisibility.postValue(isVisible)
    }

    private fun handlePokemonListResponse(response: Response<PokemonListResponse>): Resource<MutableList<Pokemon>> {
        if (response.isSuccessful) {
            response.body()?.let { pokemonListResponse ->
                _pokemonListResponse = pokemonListResponse

                val pokemonList = pokemonListResponse.results.map { pokemon ->
                    val pokemonId = pokemon.url.dropLast(1).takeLastWhile { char ->
                        char.isDigit()
                    }.toInt()

                    Pokemon(
                        pokemonId,
                        pokemon.name,
                        BASE_IMAGE_URL.plus("$pokemonId.png")
                    )
                }.toMutableList()

                _pokemonList.value?.data?.let { oldPokemonList ->
                    oldPokemonList.addAll(pokemonList)
                    return Resource.Success(oldPokemonList)
                }
                return Resource.Success(pokemonList)
            }
        }
        return Resource.Error(_pokemonList.value?.data, response.message())
    }

    private fun handlePokemonDetailResponse(response: Response<PokemonDetail>): Resource<PokemonDetail> {
        if (response.isSuccessful) {
            response.body()?.let { pokemonDetail ->
                pokemonDetail.isFavourite = pokeRepository.isFavouritePokemonById(pokemonDetail.id)
                return Resource.Success(pokemonDetail)
            }
        }
        return Resource.Error(_pokemonDetail.value?.data, response.message())
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