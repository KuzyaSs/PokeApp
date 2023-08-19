package com.example.pokeapp.domain.useCase.detail

import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.domain.repository.PokeRepository

class DeletePokemonUseCase(private val pokeRepository: PokeRepository) {
    suspend fun execute(pokemon: Pokemon) {
        pokeRepository.deletePokemon(pokemon = pokemon)
    }
}