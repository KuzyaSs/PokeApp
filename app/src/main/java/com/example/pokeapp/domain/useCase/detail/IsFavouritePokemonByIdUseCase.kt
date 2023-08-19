package com.example.pokeapp.domain.useCase.detail

import com.example.pokeapp.domain.repository.PokeRepository

class IsFavouritePokemonByIdUseCase(private val pokeRepository: PokeRepository) {
    fun execute(pokemonId: Int): Boolean {
        return pokeRepository.isFavouritePokemonById(id = pokemonId)
    }
}