package com.example.pokeapp.repository

import androidx.lifecycle.LiveData
import com.example.pokeapp.data.database.PokeDatabase
import com.example.pokeapp.data.database.model.Pokemon
import com.example.pokeapp.data.remote.PokeService
import com.example.pokeapp.data.remote.model.PokemonDetail
import com.example.pokeapp.data.remote.model.PokemonListResponse
import retrofit2.Response
import javax.inject.Inject

class PokeRepository @Inject constructor(
    private val database: PokeDatabase,
    private val pokeService: PokeService
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Response<PokemonListResponse> {
        return pokeService.getPokemonList(limit, offset)
    }

    fun getFavouritePokemonList(): LiveData<List<Pokemon>> {
        return database.getPokeDao().getFavouritePokemonList()
    }

    suspend fun getPokemonDetail(name: String): Response<PokemonDetail> {
        return pokeService.getPokemonDetail(name)
    }

    suspend fun insertPokemon(pokemon: Pokemon) {
        database.getPokeDao().insert(pokemon)
    }

    suspend fun deletePokemon(pokemon: Pokemon) {
        database.getPokeDao().delete(pokemon)
    }

    fun isFavouritePokemonById(id: Int): Boolean {
        if (database.getPokeDao().getPokemonById(id) != null) {
            return true
        }
        return false
    }
}