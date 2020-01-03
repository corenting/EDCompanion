package fr.corenting.edcompanion.models.events

import fr.corenting.edcompanion.models.NewsArticle

data class News(val success: Boolean, val articles: List<NewsArticle>)