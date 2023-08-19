package com.example.pokeapp.data.repository.mapper

interface Mapper<DataModel, DomainModel> {
    fun mapFromDataModel(dataModel: DataModel): DomainModel

    fun mapToDataModel(domainModel: DomainModel): DataModel
}