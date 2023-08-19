package com.example.pokeapp.data.repository.mapper

import com.example.pokeapp.data.database.model.PokemonEntity
import com.example.pokeapp.domain.model.Pokemon

class PokemonMapper : Mapper<PokemonEntity, Pokemon> {
    override fun mapFromDataModel(dataModel: PokemonEntity): Pokemon {
        dataModel.apply {
            return Pokemon(
                id = id,
                name = name,
                imageUrl = imageUrl
            )
        }
    }

    override fun mapToDataModel(domainModel: Pokemon): PokemonEntity {
        domainModel.apply {
            return PokemonEntity(
                id = id,
                name = name,
                imageUrl = imageUrl
            )
        }
    }
}