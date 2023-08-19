package com.example.pokeapp.di

import android.content.Context
import com.example.pokeapp.di.data.DatabaseModule
import com.example.pokeapp.di.data.MapperModule
import com.example.pokeapp.di.data.RemoteModule
import com.example.pokeapp.di.data.RepositoryModule
import com.example.pokeapp.di.domain.UseCaseModule
import com.example.pokeapp.di.presentation.ViewModelFactoryModule
import com.example.pokeapp.presentation.activity.PokeActivity
import com.example.pokeapp.presentation.fragment.DetailFragment
import com.example.pokeapp.presentation.fragment.FavouriteFragment
import com.example.pokeapp.presentation.fragment.SearchFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        DatabaseModule::class,
        RemoteModule::class,
        MapperModule::class,
        UseCaseModule::class,
        RepositoryModule::class,
        ViewModelFactoryModule::class
    ]
)
@Singleton
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    // Activity
    fun inject(activity: PokeActivity)

    // Fragments
    fun inject(fragment: SearchFragment)
    fun inject(fragment: DetailFragment)
    fun inject(fragment: FavouriteFragment)
}