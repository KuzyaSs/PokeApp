package com.example.pokeapp.di

import android.app.Application
import com.example.pokeapp.ui.PokeActivity
import com.example.pokeapp.ui.fragment.DetailFragment
import com.example.pokeapp.ui.fragment.FavouriteFragment
import com.example.pokeapp.ui.fragment.SearchFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [RoomModule::class, RemoteModule::class])
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }

    // Activity
    fun inject(activity: PokeActivity)

    // Fragments
    fun inject(fragment: SearchFragment)
    fun inject(fragment: DetailFragment)
    fun inject(fragment: FavouriteFragment)
}