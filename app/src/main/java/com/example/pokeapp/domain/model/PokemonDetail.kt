package com.example.pokeapp.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val stats: List<Stat>,
    val types: List<Type>,
) {
    var isFavourite: Boolean = false
}