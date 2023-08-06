package com.example.pokeapp.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "pokemon")
data class Pokemon(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String
) : Serializable
