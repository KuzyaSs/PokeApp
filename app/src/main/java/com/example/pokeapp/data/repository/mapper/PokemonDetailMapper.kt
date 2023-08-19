package com.example.pokeapp.data.repository.mapper

import com.example.pokeapp.data.remote.model.PokemonDetailResponse
import com.example.pokeapp.data.remote.model.Sprites
import com.example.pokeapp.domain.model.PokemonDetail
import com.example.pokeapp.domain.model.Stat
import com.example.pokeapp.domain.model.Type
import com.example.pokeapp.util.Constants.Companion.EMPTY_STRING

class PokemonDetailMapper : Mapper<PokemonDetailResponse, PokemonDetail> {
    override fun mapFromDataModel(dataModel: PokemonDetailResponse): PokemonDetail {
        dataModel.apply {
            val pokemonName = name.replaceFirstChar { firstChar ->
                firstChar.uppercaseChar()
            }
            val stats: List<Stat> = stats.map { stat ->
                Stat(name = stat.stat.name, value = stat.base_stat)
            }
            val types: List<Type> = types.map { type ->
                val typeName = type.type.name.replaceFirstChar { firstChar ->
                    firstChar.uppercaseChar()
                }
                Type(name = typeName)
            }
            return PokemonDetail(
                id = id,
                name = pokemonName,
                imageUrl = sprites.front_default,
                stats = stats,
                types = types
            )
        }
    }

    override fun mapToDataModel(domainModel: PokemonDetail): PokemonDetailResponse {
        domainModel.apply {
            return PokemonDetailResponse(
                abilities = listOf(),
                baseExperience = 0,
                forms = listOf(),
                gameIndices = listOf(),
                height = 0,
                heldItems = listOf(),
                id = id,
                isDefault = false,
                locationAreaEncounters = EMPTY_STRING,
                moves = listOf(),
                name = name,
                order = 0,
                pastTypes = listOf(),
                species = null,
                sprites = Sprites(
                    back_default = null,
                    back_female = null,
                    back_shiny = null,
                    back_shiny_female = null,
                    front_default = imageUrl,
                    front_female = null,
                    front_shiny = null,
                    front_shiny_female = null,
                    other = null,
                    versions = null
                ),
                stats = listOf(),
                types = listOf(),
                weight = 0
            )
        }
    }
}