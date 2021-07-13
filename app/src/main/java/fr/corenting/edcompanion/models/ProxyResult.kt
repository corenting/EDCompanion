package fr.corenting.edcompanion.models

data class ProxyResult<T>(val data: T?, val error: Throwable? = null)
