package com.example.pokeapp.domain.repository

import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.domain.model.PokemonDetail
import com.example.pokeapp.domain.model.PokemonListResult
import com.example.pokeapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokeRepository {
    suspend fun getPokemonListResult(limit: Int, offset: Int): Resource<PokemonListResult>

    suspend fun getPokemonDetailById(id: Int): Resource<PokemonDetail>

    suspend fun insertPokemon(pokemon: Pokemon)

    suspend fun deletePokemon(pokemon: Pokemon)

    fun getFavouritePokemonList(): Flow<List<Pokemon>>

    fun isFavouritePokemonById(id: Int): Boolean
}