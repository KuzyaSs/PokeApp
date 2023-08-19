package com.example.pokeapp.domain.useCase.search

import com.example.pokeapp.domain.model.PokemonListResult
import com.example.pokeapp.domain.repository.PokeRepository
import com.example.pokeapp.util.Constants.Companion.CONVERSION_ERROR_MESSAGE
import com.example.pokeapp.util.Constants.Companion.DEFAULT_RESPONSE_LIMIT
import com.example.pokeapp.util.Constants.Companion.DEFAULT_RESPONSE_OFFSET
import com.example.pokeapp.util.Constants.Companion.EMPTY_STRING
import com.example.pokeapp.util.Constants.Companion.NETWORK_ERROR_MESSAGE
import com.example.pokeapp.util.Resource
import java.io.IOException

class GetPokemonListResultUseCase(private val pokeRepository: PokeRepository) {
    suspend fun execute(
        pokemonListResult: PokemonListResult,
    ): Resource<PokemonListResult> {
        try {
            val newPokemonListResultResource = pokeRepository.getPokemonListResult(
                limit = getLimitFromUrl(url = pokemonListResult.nextUrl),
                offset = getOffsetFromUrl(url = pokemonListResult.nextUrl)
            )
            newPokemonListResultResource.data?.let { newPokemonListResult ->
                val combinedPokemonListResult = PokemonListResult(
                    nextUrl = newPokemonListResult.nextUrl,
                    previousUrl = newPokemonListResult.previousUrl,
                    pokemonList = pokemonListResult.pokemonList.plus(newPokemonListResult.pokemonList)
                )
                return Resource.Success(data = combinedPokemonListResult)
            }
            return Resource.Error(
                data = pokemonListResult,
                message = newPokemonListResultResource.message ?: EMPTY_STRING
            )
        } catch (throwable: Throwable) {
            return when (throwable) {
                is IOException -> Resource.Error(
                    data = pokemonListResult,
                    message = NETWORK_ERROR_MESSAGE
                )

                else -> Resource.Error(
                    data = pokemonListResult,
                    message = CONVERSION_ERROR_MESSAGE
                )
            }
        }
    }

    private fun getLimitFromUrl(url: String): Int {
        return if (url.isEmpty()) {
            DEFAULT_RESPONSE_LIMIT
        } else {
            url.takeLastWhile { char ->
                char.isDigit()
            }.toInt()
        }
    }

    private fun getOffsetFromUrl(url: String): Int {
        return if (url.isEmpty()) {
            DEFAULT_RESPONSE_OFFSET
        } else {
            url.dropWhile { char ->
                char != '='
            }.drop(1).takeWhile { char ->
                char.isDigit()
            }.toInt()
        }
    }
}