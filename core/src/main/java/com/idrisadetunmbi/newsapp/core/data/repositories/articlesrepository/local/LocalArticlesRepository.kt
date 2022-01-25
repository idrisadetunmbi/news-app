package com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.local

import com.idrisadetunmbi.newsapp.core.models.Article
import kotlinx.coroutines.flow.Flow

interface LocalArticlesRepository {
    fun getUnremovedArticles(): Flow<List<Article>>

    suspend fun saveArticles(articles: List<Article>)

    fun getArticlesById(ids: List<String>): Flow<List<Article>>

    fun getAllArticles(): Flow<List<Article>>

    suspend fun removeArticles(articles: List<Article>)

    suspend fun updateArticles(articles: List<Article>)
}
