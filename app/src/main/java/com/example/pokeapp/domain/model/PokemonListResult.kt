package com.example.pokeapp.domain.model

data class PokemonListResult(
    val nextUrl: String,
    val previousUrl: String,
    val pokemonList: List<Pokemon>
)