package com.example.pokeapp.data.remote.model

data class PokemonListResultResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Result>
)