package com.example.pokeapp.di.data

import com.example.pokeapp.data.database.PokeDao
import com.example.pokeapp.data.remote.PokeService
import com.example.pokeapp.data.repository.PokeRepositoryImpl
import com.example.pokeapp.data.repository.mapper.PokemonDetailMapper
import com.example.pokeapp.data.repository.mapper.PokemonListResultMapper
import com.example.pokeapp.data.repository.mapper.PokemonMapper
import com.example.pokeapp.domain.repository.PokeRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun providePokeRepository(
        pokeDao: PokeDao,
        pokeService: PokeService,
        pokemonMapper: PokemonMapper,
        pokemonListResultMapper: PokemonListResultMapper,
        pokemonDetailMapper: PokemonDetailMapper
    ): PokeRepository {
        return PokeRepositoryImpl(
            pokeDao = pokeDao,
            pokeService = pokeService,
            pokemonMapper = pokemonMapper,
            pokemonListResultMapper = pokemonListResultMapper,
            pokemonDetailMapper = pokemonDetailMapper
        )
    }
}