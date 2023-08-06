package com.example.pokeapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.pokeapp.data.database.PokeDatabase
import com.example.pokeapp.data.remote.PokeService
import com.example.pokeapp.repository.PokeRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides
    fun provideDatabase(application: Application): PokeDatabase {
        return Room.databaseBuilder(
            application,
            PokeDatabase::class.java,
            "poke_database"
        ).build()
    }
}