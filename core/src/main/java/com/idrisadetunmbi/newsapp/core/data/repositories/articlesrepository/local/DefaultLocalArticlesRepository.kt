package com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.local

import com.idrisadetunmbi.newsapp.core.data.database.daos.ArticlesDao
import com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.DataMapper
import com.idrisadetunmbi.newsapp.core.models.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultLocalArticlesRepository @Inject constructor(
    private val articlesDao: ArticlesDao,
    private val dataMapper: DataMapper,
) : LocalArticlesRepository {

    override fun getUnremovedArticles(): Flow<List<Article>> {
        return articlesDao.getUnremovedArticles()
            .map { articles -> articles.map { dataMapper.mapArticle(it) } }
    }

    override suspend fun saveArticles(articles: List<Article>) {
        val dbArticles = articles.map { dataMapper.mapArticle(it) }.toTypedArray()
        articlesDao.insertArticles(*dbArticles)
    }

    override fun getArticlesById(ids: List<String>): Flow<List<Article>> {
        return articlesDao.getArticlesByIds(ids)
            .map { it.map { entity -> dataMapper.mapArticle(entity) } }
    }

    override fun getAllArticles(): Flow<List<Article>> {
        return articlesDao.getArticles()
            .map { it.map { article -> dataMapper.mapArticle(article) } }
    }

    override suspend fun removeArticles(articles: List<Article>) {
        articlesDao.clearArticles(*articles.map { dataMapper.mapArticle(it) }.toTypedArray())
    }

    override suspend fun updateArticles(articles: List<Article>) {
        articlesDao.updateArticles(*articles.map { dataMapper.mapArticle(it) }.toTypedArray())
    }
}
