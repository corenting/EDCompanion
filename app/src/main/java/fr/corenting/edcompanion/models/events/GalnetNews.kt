package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.GalnetArticle

data class GalnetNews(val success: Boolean, val articles: List<GalnetArticle>)