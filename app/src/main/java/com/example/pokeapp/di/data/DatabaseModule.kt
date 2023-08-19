package com.example.pokeapp.di.data

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.pokeapp.data.database.PokeDao
import com.example.pokeapp.data.database.PokeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): PokeDatabase {
        return Room.databaseBuilder(
            context,
            PokeDatabase::class.java,
            "poke_database"
        ).build()
    }
    @Singleton
    @Provides
    fun providePokeDao(database: PokeDatabase): PokeDao {
        return database.getPokeDao()
    }
}