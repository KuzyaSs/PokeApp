package com.example.pokeapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokeapp.data.database.model.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemonEntity: PokemonEntity)

    @Delete
    suspend fun delete(pokemonEntity: PokemonEntity)

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getPokemonById(id: Int): PokemonEntity?

    @Query("SELECT * FROM pokemon")
    fun getFavouritePokemonList(): Flow<List<PokemonEntity>>
}