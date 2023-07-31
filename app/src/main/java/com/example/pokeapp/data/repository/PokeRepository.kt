package com.example.pokeapp.data.repository

import com.example.pokeapp.data.model.Pokemon
import com.example.pokeapp.data.model.PokemonList
import com.example.pokeapp.data.remote.PokeService
import com.example.pokeapp.util.Resource
import javax.inject.Inject

class PokeRepository(
    @Inject private val pokeService: PokeService
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        return try {
            val response = pokeService.getPokemonList(limit, offset)
            Resource.Success(response)
        } catch (exception: Exception) {
            Resource.Error(message = "An unknown error occurred")
        }
    }

    suspend fun getPokemonInfo(name: String): Resource<Pokemon> {
        return try {
            val response = pokeService.getPokemonInfo(name)
            Resource.Success(response)
        } catch (exception: Exception) {
            Resource.Error(message = "An unknown error occurred")
        }
    }
}