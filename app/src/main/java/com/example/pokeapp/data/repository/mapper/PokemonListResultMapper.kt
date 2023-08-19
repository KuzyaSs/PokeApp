package com.example.pokeapp.data.repository.mapper

import com.example.pokeapp.data.remote.model.PokemonListResultResponse
import com.example.pokeapp.data.remote.model.Result
import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.domain.model.PokemonListResult
import com.example.pokeapp.util.Constants.Companion.BASE_IMAGE_URL
import com.example.pokeapp.util.Constants.Companion.BASE_POKEMON_URL
import com.example.pokeapp.util.Constants.Companion.EMPTY_STRING

class PokemonListResultMapper : Mapper<PokemonListResultResponse, PokemonListResult> {
    override fun mapFromDataModel(dataModel: PokemonListResultResponse): PokemonListResult {
        dataModel.apply {
            val pokemonList = results.map { result ->
                val pokemonId = result.url.dropLast(1).takeLastWhile { char ->
                    char.isDigit()
                }.toInt()
                val pokemonName = result.name.replaceFirstChar { firstChar ->
                    firstChar.uppercaseChar()
                }
                Pokemon(
                    id = pokemonId,
                    name = pokemonName,
                    imageUrl = BASE_IMAGE_URL.plus("$pokemonId.png")
                )
            }
            return PokemonListResult(
                nextUrl = next ?: EMPTY_STRING,
                previousUrl = previous ?: EMPTY_STRING,
                pokemonList = pokemonList
            )
        }
    }

    override fun mapToDataModel(domainModel: PokemonListResult): PokemonListResultResponse {
        domainModel.apply {
            val resultList = pokemonList.map { pokemon ->
                Result(
                    name = pokemon.name,
                    url = BASE_POKEMON_URL.plus(pokemon.id.toString())
                )
            }
            return PokemonListResultResponse(
                count = pokemonList.size,
                next = nextUrl,
                previous = previousUrl,
                results = resultList
            )
        }
    }
}