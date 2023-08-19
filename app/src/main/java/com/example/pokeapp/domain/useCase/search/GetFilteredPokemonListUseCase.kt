package com.example.pokeapp.domain.useCase.search

import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.util.Constants

class GetFilteredPokemonListUseCase {
    fun execute(
        pokemonList: List<Pokemon>,
        searchString: String,
        errorMessage: String
    ): List<Pokemon> {
        val newPokemonList = mutableListOf<Pokemon>()
        if (searchString.isNotBlank()) {
            newPokemonList.addAll(pokemonList.filter { pokemon ->
                pokemon.name.startsWith(searchString)
            })
        } else {
            newPokemonList.addAll(pokemonList)
        }
        if (errorMessage.isNotBlank() && searchString.isBlank()) {
            for (i in 0 until Constants.DEFAULT_RESPONSE_LIMIT) {
                val errorPokemon = Pokemon(
                    Constants.ERROR_POKEMON_ID,
                    errorMessage,
                    errorMessage
                )
                newPokemonList.add(errorPokemon)
            }
        }
        return newPokemonList
    }
}