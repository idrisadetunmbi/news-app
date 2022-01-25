package com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.room.withTransaction
import com.idrisadetunmbi.newsapp.core.data.Resource
import com.idrisadetunmbi.newsapp.core.data.dataStore
import com.idrisadetunmbi.newsapp.core.data.database.Database
import com.idrisadetunmbi.newsapp.core.data.database.daos.ArticlesDao
import com.idrisadetunmbi.newsapp.core.data.database.entities.ArticleEntity
import com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.remote.ArticlesWebService
import com.idrisadetunmbi.newsapp.core.models.Article
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepositoryImpl @Inject constructor(
    private val articlesWebService: ArticlesWebService,
    private val articlesDao: ArticlesDao,
    private val database: Database,
    private val mapper: DataMapper,
    @ApplicationContext private val context: Context,
) : ArticlesRepository {
    override fun getArticles(maxDataSet: Int): Flow<Resource<List<Article>>> {
        return flow {
            val query = { articlesDao.getUnremovedArticles() }
            val dbData = query().first()
            val data = if (dbData.size < maxDataSet) {
                emit(Resource.loading(dbData.mapArticles()))
                try {
                    val entities = articlesWebService.getAllArticles().articles
                        .mapNotNull { mapper.mapArticle(it) }
                    articlesDao.insertArticles(*entities.toTypedArray())
                    query().map { Resource.success(it.mapArticles()) }
                } catch (ex: HttpException) {
                    query().map { Resource.error(it.mapArticles(), ex) }
                }
            } else {
                query().map { Resource.success(it.mapArticles()) }
            }
            emitAll(data)
        }
    }

    override suspend fun bookmarkArticle(id: String) {
        context.dataStore.edit {
            val currentBookmarks = it[BOOKMARKED_IDS_PREF_KEY].orEmpty()
            val updatedBookmarks = currentBookmarks.toMutableSet()
                .apply { if (contains(id)) remove(id) else add(id) }
            it[BOOKMARKED_IDS_PREF_KEY] = updatedBookmarks
        }
    }

    override fun getBookmarkedArticleIds(): Flow<List<String>> {
        return context.dataStore.data.map {
            it[BOOKMARKED_IDS_PREF_KEY].orEmpty().toList()
        }
    }

    override fun getArticlesByIds(ids: List<String>): Flow<List<Article>> {
        return articlesDao.getArticlesByIds(ids).map { it.mapArticles() }
    }

    override suspend fun refreshArticles(maxDataSet: Int) {
        val newArticles = articlesWebService.getAllArticles().articles
            .mapNotNull { mapper.mapArticle(it) }
        val existingArticles = articlesDao.getArticles().first()

        database.withTransaction {
            if (existingArticles.size > maxDataSet) {
                val (bookmarkedArticles, otherArticles) = existingArticles
                    .partition { getBookmarkedArticleIds().first().contains(it.id) }

                articlesDao.clearArticles(*otherArticles.toTypedArray())

                val removedBookmarks = bookmarkedArticles.map { it.copy(removed = true) }
                articlesDao.updateArticles(*removedBookmarks.toTypedArray())
            }
            articlesDao.insertArticles(*newArticles.toTypedArray())
        }
    }

    override fun getArticle(id: String): Flow<Article> {
        return articlesDao.getArticle(id).map { mapper.mapArticle(it) }
    }

    private fun List<ArticleEntity>.mapArticles(): List<Article> {
        return map { mapper.mapArticle(it) }
    }

    companion object {
        val BOOKMARKED_IDS_PREF_KEY = stringSetPreferencesKey("bookmarked_articles")
    }
}
