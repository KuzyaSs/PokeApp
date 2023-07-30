package com.example.pokeapp.data.remote

import com.example.pokeapp.data.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET

interface PokeService {
    @GET("pokemon")
    suspend fun getPokemon(): Response<PokemonResponse>
}