package com.example.pokeapp.util

import com.example.pokeapp.R

class Constants {
    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
        const val BASE_IMAGE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"

        const val DEFAULT_RESPONSE_LIMIT = 20
        const val DEFAULT_RESPONSE_OFFSET = 0

        const val POKEMON_LIST_LOAD_DELAY = 500L

        const val NETWORK_ERROR_MESSAGE = "Network error"
        const val CONVERSION_ERROR_MESSAGE = "Conversion error"
        const val ERROR_POKEMON_ID = -1

        const val SPAN_COUNT = 2

        val typeToBackgroundResourceMap = mapOf(
            "Normal" to R.drawable.background_normal_type,
            "Fire" to R.drawable.background_fire_type,
            "Water" to R.drawable.background_water_type,
            "Grass" to R.drawable.background_grass_type,
            "Electric" to R.drawable.background_electric_type,
            "Ice" to R.drawable.background_ice_type,
            "Fighting" to R.drawable.background_fighting_type,
            "Poison" to R.drawable.background_poison_type,
            "Ground" to R.drawable.background_ground_type,
            "Flying" to R.drawable.background_flying_type,
            "Psychic" to R.drawable.background_psychic_type,
            "Bug" to R.drawable.background_bug_type,
            "Rock" to R.drawable.background_rock_type,
            "Ghost" to R.drawable.background_ghost_type,
            "Dark" to R.drawable.background_dark_type,
            "Dragon" to R.drawable.background_dragon_type,
            "Steel" to R.drawable.background_steel_type,
            "Fairy" to R.drawable.background_fairy_type
        )
        val typeToColorMap = mapOf(
            "Normal" to R.color.normal,
            "Fire" to R.color.fire,
            "Water" to R.color.water,
            "Grass" to R.color.grass,
            "Electric" to R.color.electric,
            "Ice" to R.color.ice,
            "Fighting" to R.color.fighting,
            "Poison" to R.color.poison,
            "Ground" to R.color.ground,
            "Flying" to R.color.flying,
            "Psychic" to R.color.psychic,
            "Bug" to R.color.bug,
            "Rock" to R.color.rock,
            "Ghost" to R.color.ghost,
            "Dark" to R.color.dark,
            "Dragon" to R.color.dragon,
            "Steel" to R.color.steel,
            "Fairy" to R.color.fairy
        )
    }
}