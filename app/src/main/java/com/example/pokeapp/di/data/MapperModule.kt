package com.example.pokeapp.di.data

import com.example.pokeapp.data.repository.mapper.PokemonDetailMapper
import com.example.pokeapp.data.repository.mapper.PokemonListResultMapper
import com.example.pokeapp.data.repository.mapper.PokemonMapper
import dagger.Module
import dagger.Provides

@Module
class MapperModule {
    @Provides
    fun providePokemonMapper(): PokemonMapper {
        return PokemonMapper()
    }

    @Provides
    fun providePokemonListResultMapper(): PokemonListResultMapper {
        return PokemonListResultMapper()
    }

    @Provides
    fun providePokemonDetailMapper(): PokemonDetailMapper {
        return PokemonDetailMapper()
    }
}