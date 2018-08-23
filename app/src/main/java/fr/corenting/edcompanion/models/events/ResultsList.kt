package fr.corenting.edcompanion.models.events

data class ResultsList<TDataType>(val success: Boolean, val results: List<TDataType>)