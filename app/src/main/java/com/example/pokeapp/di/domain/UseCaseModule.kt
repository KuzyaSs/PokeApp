package com.example.pokeapp.di.domain

import com.example.pokeapp.domain.repository.PokeRepository
import com.example.pokeapp.domain.useCase.detail.DeletePokemonUseCase
import com.example.pokeapp.domain.useCase.detail.GetPokemonDetailByIdUseCase
import com.example.pokeapp.domain.useCase.detail.InsertPokemonUseCase
import com.example.pokeapp.domain.useCase.detail.IsFavouritePokemonByIdUseCase
import com.example.pokeapp.domain.useCase.favourite.GetFilteredFavouritePokemonListUseCase
import com.example.pokeapp.domain.useCase.favourite.GetFavouritePokemonListUseCase
import com.example.pokeapp.domain.useCase.search.GetFilteredPokemonListUseCase
import com.example.pokeapp.domain.useCase.search.GetPokemonListResultUseCase
import com.example.pokeapp.domain.useCase.search.IsLastPageUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {
    @Provides
    fun provideGetPokemonListResultUseCase(pokeRepository: PokeRepository): GetPokemonListResultUseCase {
        return GetPokemonListResultUseCase(pokeRepository = pokeRepository)
    }

    @Provides
    fun provideGetFilteredPokemonListUseCase(): GetFilteredPokemonListUseCase {
        return GetFilteredPokemonListUseCase()
    }

    @Provides
    fun provideIsLastPageUseCase(): IsLastPageUseCase {
        return IsLastPageUseCase()
    }

    @Provides
    fun provideGetPokemonDetailByIdUseCase(
        pokeRepository: PokeRepository,
        isFavouritePokemonByIdUseCase: IsFavouritePokemonByIdUseCase
    ): GetPokemonDetailByIdUseCase {
        return GetPokemonDetailByIdUseCase(
            pokeRepository = pokeRepository,
            isFavouritePokemonByIdUseCase = isFavouritePokemonByIdUseCase
        )
    }

    @Provides
    fun provideIsFavouritePokemonByIdUseCase(pokeRepository: PokeRepository): IsFavouritePokemonByIdUseCase {
        return IsFavouritePokemonByIdUseCase(pokeRepository = pokeRepository)
    }

    @Provides
    fun provideInsertPokemonUseCase(pokeRepository: PokeRepository): InsertPokemonUseCase {
        return InsertPokemonUseCase(pokeRepository = pokeRepository)
    }

    @Provides
    fun provideDeletePokemonUseCase(pokeRepository: PokeRepository): DeletePokemonUseCase {
        return DeletePokemonUseCase(pokeRepository = pokeRepository)
    }

    @Provides
    fun provideGetFavouritePokemonListUseCase(pokeRepository: PokeRepository): GetFavouritePokemonListUseCase {
        return GetFavouritePokemonListUseCase(pokeRepository = pokeRepository)
    }

    @Provides
    fun provideGetFilteredFavouritePokemonListUseCase(): GetFilteredFavouritePokemonListUseCase {
        return GetFilteredFavouritePokemonListUseCase()
    }
}