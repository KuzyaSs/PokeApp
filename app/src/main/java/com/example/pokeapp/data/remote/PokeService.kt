package com.example.pokeapp.data.remote

import com.example.pokeapp.data.remote.model.PokemonDetailResponse
import com.example.pokeapp.data.remote.model.PokemonListResultResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {
    @GET("pokemon")
    suspend fun getPokemonListResultResponse(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<PokemonListResultResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetailById(
        @Path("id") id: Int
    ): Response<PokemonDetailResponse>
}