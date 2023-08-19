package com.example.pokeapp.data.repository

import android.util.Log
import com.example.pokeapp.data.database.PokeDao
import com.example.pokeapp.data.remote.PokeService
import com.example.pokeapp.data.repository.mapper.PokemonDetailMapper
import com.example.pokeapp.data.repository.mapper.PokemonListResultMapper
import com.example.pokeapp.data.repository.mapper.PokemonMapper
import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.domain.model.PokemonDetail
import com.example.pokeapp.domain.model.PokemonListResult
import com.example.pokeapp.domain.repository.PokeRepository
import com.example.pokeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokeRepositoryImpl(
    private val pokeDao: PokeDao,
    private val pokeService: PokeService,
    private val pokemonMapper: PokemonMapper,
    private val pokemonListResultMapper: PokemonListResultMapper,
    private val pokemonDetailMapper: PokemonDetailMapper
) : PokeRepository {
    override suspend fun getPokemonListResult(
        limit: Int,
        offset: Int
    ): Resource<PokemonListResult> {
        val response = pokeService.getPokemonListResultResponse(limit = limit, offset = offset)
        if (response.isSuccessful) {
            response.body()?.let { pokemonListResultResponse ->
                return Resource.Success(
                    data = pokemonListResultMapper.mapFromDataModel(pokemonListResultResponse)
                )
            }
        }
        return Resource.Error(data = null, message = response.message())
    }

    override suspend fun getPokemonDetailById(id: Int): Resource<PokemonDetail> {
        val response = pokeService.getPokemonDetailById(id = id)
        if (response.isSuccessful) {
            response.body()?.let { pokemonDetailResponse ->
                return Resource.Success(
                    data = pokemonDetailMapper.mapFromDataModel(
                        pokemonDetailResponse
                    )
                )
            }
        }
        return Resource.Error(data = null, message = response.message())
    }

    override suspend fun insertPokemon(pokemon: Pokemon) {
        pokeDao.insert(pokemonEntity = pokemonMapper.mapToDataModel(domainModel = pokemon))
    }

    override suspend fun deletePokemon(pokemon: Pokemon) {
        pokeDao.delete(pokemonEntity = pokemonMapper.mapToDataModel(domainModel = pokemon))
    }

    override fun getFavouritePokemonList(): Flow<List<Pokemon>> {
        return pokeDao.getFavouritePokemonList().map { pokemonEntityList ->
            pokemonEntityList.map { pokemonEntity ->
                pokemonMapper.mapFromDataModel(dataModel = pokemonEntity)
            }
        }
    }

    override fun isFavouritePokemonById(id: Int): Boolean {
        return pokeDao.getPokemonById(id = id) != null
    }
}