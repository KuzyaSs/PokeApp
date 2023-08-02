package com.example.pokeapp.di

import com.example.pokeapp.ui.PokeActivity
import com.example.pokeapp.ui.fragment.DetailFragment
import com.example.pokeapp.ui.fragment.FavouriteFragment
import com.example.pokeapp.ui.fragment.SearchFragment
import dagger.Component

@Component(modules = [RemoteModule::class, RoomModule::class])
interface ApplicationComponent {
    // Activity
    fun inject(activity: PokeActivity)

    // Fragments
    fun inject(fragment: SearchFragment)
    fun inject(fragment: DetailFragment)
    fun inject(fragment: FavouriteFragment)
}