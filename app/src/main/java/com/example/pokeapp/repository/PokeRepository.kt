package com.example.pokeapp.repository

import androidx.lifecycle.LiveData
import com.example.pokeapp.data.database.PokeDao
import com.example.pokeapp.data.database.model.Pokemon
import com.example.pokeapp.data.remote.PokeService
import com.example.pokeapp.data.remote.model.PokemonDetail
import com.example.pokeapp.data.remote.model.PokemonListResponse
import retrofit2.Response
import javax.inject.Inject

class PokeRepository @Inject constructor(
    private val pokeDao: PokeDao,
    private val pokeService: PokeService
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Response<PokemonListResponse> {
        return pokeService.getPokemonList(limit, offset)
    }

    fun getFavouritePokemonList(): LiveData<List<Pokemon>> {
        return pokeDao.getPokemonList()
    }

    suspend fun getPokemonDetail(name: String): Response<PokemonDetail> {
        return pokeService.getPokemonDetail(name)
    }

    suspend fun insertPokemon(pokemon: Pokemon) {
        pokeDao.insert(pokemon)
    }

    suspend fun deletePokemon(pokemon: Pokemon) {
        pokeDao.delete(pokemon)
    }

    fun isFavouritePokemonById(id: Int): Boolean {
        if (pokeDao.getPokemonById(id) != null) {
            return true
        }
        return false
    }
}