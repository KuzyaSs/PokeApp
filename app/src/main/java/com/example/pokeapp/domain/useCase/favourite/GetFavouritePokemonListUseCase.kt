package com.example.pokeapp.domain.useCase.favourite

import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.domain.repository.PokeRepository
import kotlinx.coroutines.flow.Flow

class GetFavouritePokemonListUseCase(private val pokeRepository: PokeRepository) {
    fun execute(): Flow<List<Pokemon>> {
        return pokeRepository.getFavouritePokemonList()
    }
}