package com.example.pokeapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokeapp.data.database.model.Pokemon

@Database(
    entities = [Pokemon::class],
    version = 1,
    exportSchema = false
)
abstract class PokeDatabase : RoomDatabase() {
    abstract fun getPokeDao(): PokeDao
}