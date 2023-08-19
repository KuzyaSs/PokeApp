package com.example.pokeapp.domain.useCase.detail

import com.example.pokeapp.domain.model.PokemonDetail
import com.example.pokeapp.domain.repository.PokeRepository
import com.example.pokeapp.util.Constants.Companion.CONVERSION_ERROR_MESSAGE
import com.example.pokeapp.util.Constants.Companion.NETWORK_ERROR_MESSAGE
import com.example.pokeapp.util.Resource
import java.io.IOException

class GetPokemonDetailByIdUseCase(
    private val pokeRepository: PokeRepository,
    private val isFavouritePokemonByIdUseCase: IsFavouritePokemonByIdUseCase
) {
    suspend fun execute(id: Int): Resource<PokemonDetail> {
        return try {
            val pokemonDetailResource = pokeRepository.getPokemonDetailById(id = id)
            pokemonDetailResource.data?.let { pokemonDetail ->
                pokemonDetail.isFavourite = isFavouritePokemonByIdUseCase.execute(pokemonDetail.id)
            }
            pokemonDetailResource
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Resource.Error(
                    data = null,
                    message = NETWORK_ERROR_MESSAGE
                )

                else -> Resource.Error(
                    data = null,
                    message = CONVERSION_ERROR_MESSAGE
                )
            }
        }
    }
}