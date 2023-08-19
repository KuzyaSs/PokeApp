package com.example.pokeapp.presentation.viewModel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokeapp.domain.useCase.detail.DeletePokemonUseCase
import com.example.pokeapp.domain.useCase.detail.GetPokemonDetailByIdUseCase
import com.example.pokeapp.domain.useCase.detail.InsertPokemonUseCase

class DetailViewModelFactory(
    private val getPokemonDetailByIdUseCase: GetPokemonDetailByIdUseCase,
    private val insertPokemonUseCase: InsertPokemonUseCase,
    private val deletePokemonUseCase: DeletePokemonUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(
                getPokemonDetailByIdUseCase = getPokemonDetailByIdUseCase,
                insertPokemonUseCase = insertPokemonUseCase,
                deletePokemonUseCase = deletePokemonUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}