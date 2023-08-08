package com.example.pokeapp.di

import android.app.Application
import androidx.room.Room
import com.example.pokeapp.data.database.PokeDao
import com.example.pokeapp.data.database.PokeDatabase
import dagger.Module
import dagger.Provides

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

    @Provides
    fun providePokeDao(database: PokeDatabase): PokeDao {
        return database.getPokeDao()
    }
}