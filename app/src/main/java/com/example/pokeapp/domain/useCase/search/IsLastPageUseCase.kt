package com.example.pokeapp.domain.useCase.search

import com.example.pokeapp.domain.model.PokemonListResult

class IsLastPageUseCase {
    fun execute(pokemonListResult: PokemonListResult): Boolean {
        return pokemonListResult.previousUrl.isNotEmpty() && pokemonListResult.nextUrl.isEmpty()
    }
}
