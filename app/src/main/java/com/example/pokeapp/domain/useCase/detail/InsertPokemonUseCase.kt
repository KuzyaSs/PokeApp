package com.example.pokeapp.domain.useCase.detail

import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.domain.repository.PokeRepository

class InsertPokemonUseCase(private val pokeRepository: PokeRepository) {
    suspend fun execute(pokemon: Pokemon) {
        pokeRepository.insertPokemon(pokemon = pokemon)
    }
}