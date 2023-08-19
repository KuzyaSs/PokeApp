package com.example.pokeapp.di.presentation

import com.example.pokeapp.domain.useCase.detail.DeletePokemonUseCase
import com.example.pokeapp.domain.useCase.detail.GetPokemonDetailByIdUseCase
import com.example.pokeapp.domain.useCase.detail.InsertPokemonUseCase
import com.example.pokeapp.domain.useCase.favourite.GetFilteredFavouritePokemonListUseCase
import com.example.pokeapp.domain.useCase.favourite.GetFavouritePokemonListUseCase
import com.example.pokeapp.domain.useCase.search.GetFilteredPokemonListUseCase
import com.example.pokeapp.domain.useCase.search.GetPokemonListResultUseCase
import com.example.pokeapp.domain.useCase.search.IsLastPageUseCase
import com.example.pokeapp.presentation.viewModel.base.BaseViewModelFactory
import com.example.pokeapp.presentation.viewModel.detail.DetailViewModelFactory
import com.example.pokeapp.presentation.viewModel.favourite.FavouriteViewModelFactory
import com.example.pokeapp.presentation.viewModel.search.SearchViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelFactoryModule {
    @Provides
    fun provideBaseViewModelFactory(): BaseViewModelFactory {
        return BaseViewModelFactory()
    }

    @Provides
    fun provideSearchViewModelFactory(
        getPokemonListResultUseCase: GetPokemonListResultUseCase,
        getFilteredPokemonListUseCase: GetFilteredPokemonListUseCase,
        isLastPageUseCase: IsLastPageUseCase,
    ): SearchViewModelFactory {
        return SearchViewModelFactory(
            getPokemonListResultUseCase = getPokemonListResultUseCase,
            getFilteredPokemonListUseCase = getFilteredPokemonListUseCase,
            isLastPageUseCase = isLastPageUseCase
        )
    }

    @Provides
    fun provideFavouriteViewModelFactory(
        getFavouritePokemonListUseCase: GetFavouritePokemonListUseCase,
        getFilteredFavouritePokemonListUseCase: GetFilteredFavouritePokemonListUseCase
    ): FavouriteViewModelFactory {
        return FavouriteViewModelFactory(
            getFavouritePokemonListUseCase = getFavouritePokemonListUseCase,
            getFilteredFavouritePokemonListUseCase = getFilteredFavouritePokemonListUseCase
        )
    }

    @Provides
    fun provideDetailViewModelFactory(
        getPokemonDetailByIdUseCase: GetPokemonDetailByIdUseCase,
        insertPokemonUseCase: InsertPokemonUseCase,
        deletePokemonUseCase: DeletePokemonUseCase
    ): DetailViewModelFactory {
        return DetailViewModelFactory(
            getPokemonDetailByIdUseCase = getPokemonDetailByIdUseCase,
            insertPokemonUseCase = insertPokemonUseCase,
            deletePokemonUseCase = deletePokemonUseCase
        )
    }
}