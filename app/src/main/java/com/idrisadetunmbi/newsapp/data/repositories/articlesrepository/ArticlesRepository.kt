package com.idrisadetunmbi.newsapp.data.repositories.articlesrepository

import com.idrisadetunmbi.newsapp.data.Resource
import com.idrisadetunmbi.newsapp.models.Article
import kotlinx.coroutines.flow.Flow

interface ArticlesRepository {
    fun getArticles(maxDataSet: Int = MAX_DATA_SET): Flow<Resource<List<Article>>>

    suspend fun bookmarkArticle(id: String)

    fun getBookmarkedArticleIds(): Flow<List<String>>

    fun getArticlesByIds(ids: List<String>): Flow<List<Article>>

    suspend fun refreshArticles(maxDataSet: Int = MAX_DATA_SET)

    companion object {
        const val MAX_DATA_SET = 500
    }
}
