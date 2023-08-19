package com.example.pokeapp.domain.model

import java.io.Serializable

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String
) : Serializable