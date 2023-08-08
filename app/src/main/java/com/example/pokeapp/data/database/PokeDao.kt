package com.example.pokeapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokeapp.data.database.model.Pokemon

@Dao
interface PokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon)

    @Delete
    suspend fun delete(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getPokemonById(id: Int): Pokemon?

    @Query("SELECT * FROM pokemon")
    fun getPokemonList(): LiveData<List<Pokemon>>
}