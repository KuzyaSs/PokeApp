package com.example.pokeapp.data.repository

import com.example.pokeapp.data.remote.PokeService
import com.example.pokeapp.data.remote.model.PokemonDetail
import com.example.pokeapp.data.remote.model.PokemonListResponse
import retrofit2.Response

class PokeRepository(private val pokeService: PokeService) {
    suspend fun getPokemonList(limit: Int, offset: Int): Response<PokemonListResponse> {
        return pokeService.getPokemonList(limit, offset)
    }

    suspend fun getPokemonDetail(name: String): Response<PokemonDetail> {
        return pokeService.getPokemonDetail(name)
    }
}