package com.example.pokeapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    val abilities: List<Ability>,
    @SerializedName("base_experience") val baseExperience: Int,
    val forms: List<Form>,
    @SerializedName("game_indices") val gameIndices: List<GameIndice>,
    val height: Int,
    @SerializedName("held_items") val heldItems: List<HeldItem>,
    val id: Int,
    @SerializedName("is_default") val isDefault: Boolean,
    @SerializedName("location_area_encounters") val locationAreaEncounters: String,
    val moves: List<Move>,
    val name: String,
    val order: Int,
    @SerializedName("past_types") val pastTypes: List<Any>,
    val species: Species?,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int,
)