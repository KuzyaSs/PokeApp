package com.example.pokeapp.domain.useCase.favourite

import com.example.pokeapp.domain.model.Pokemon

class GetFilteredFavouritePokemonListUseCase {
    fun execute(pokemonList: List<Pokemon>, searchString: String): List<Pokemon> {
        return if (searchString.isNotBlank()) {
            pokemonList.filter { pokemon ->
                pokemon.name.startsWith(searchString)
            }
        } else {
            pokemonList
        }
    }
}