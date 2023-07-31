package com.example.pokeapp.data.repository

import com.example.pokeapp.data.remote.model.Pokemon
import com.example.pokeapp.data.remote.model.PokemonResponse
import com.example.pokeapp.data.remote.PokeService
import com.example.pokeapp.util.Resource
import retrofit2.Response
import javax.inject.Inject

class PokeRepository(
    @Inject private val pokeService: PokeService
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Response<PokemonResponse> {
        return pokeService.getPokemonList(limit, offset)

//        return try {
//            val response = pokeService.getPokemonList(limit, offset)
//            Resource.Success(response)
//        } catch (exception: Exception) {
//            Resource.Error(message = "An unknown error occurred")
//        }
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