package com.example.pokeapp.data.remote.model

data class HeldItem(
    val item: Item,
    val version_details: List<VersionDetail>
)