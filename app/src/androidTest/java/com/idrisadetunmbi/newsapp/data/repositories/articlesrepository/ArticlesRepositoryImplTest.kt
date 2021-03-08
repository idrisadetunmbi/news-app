package com.idrisadetunmbi.newsapp.data.repositories.articlesrepository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idrisadetunmbi.newsapp.data.Resource
import com.idrisadetunmbi.newsapp.data.dataStore
import com.idrisadetunmbi.newsapp.data.database.Database
import com.idrisadetunmbi.newsapp.data.database.entities.ArticleEntity
import com.idrisadetunmbi.newsapp.data.repositories.articlesrepository.remote.AllArticlesResponseModel
import com.idrisadetunmbi.newsapp.data.repositories.articlesrepository.remote.ArticlesWebService
import com.idrisadetunmbi.newsapp.models.Article
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class ArticlesRepositoryImplTest {
    private lateinit var context: Context
    private lateinit var db: Database

    private val dataMapper = DataMapper()
    private val webService = object : ArticlesWebService {
        override suspend fun getAllArticles(): AllArticlesResponseModel {
            return AllArticlesResponseModel()
        }
    }

    @Before
    fun before() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
    }

    @Test
    fun test_getArticles_network_success_emit_sequence() {
        runBlocking {
            val sut = ArticlesRepositoryImpl(webService, db, dataMapper, context)
            val result = sut.getArticles().take(2).toList()
            assertEquals(result.first(), Resource.loading(listOf<Article>()))
            assertEquals(result[1], Resource.success(listOf<Article>()))
        }
    }

    @Test
    fun test_getArticles_network_error_emit_sequence() {
        runBlocking {
            val response = Response.error<String>(400, "".toResponseBody())
            val sut = ArticlesRepositoryImpl(object : ArticlesWebService {
                override suspend fun getAllArticles(): AllArticlesResponseModel {
                    throw HttpException(response)
                }
            }, db, dataMapper, context)
            val result = sut.getArticles().take(2).toList()
            assertEquals(result.first(), Resource.loading(listOf<Article>()))
            assertTrue(result[1].status == Resource.Status.ERROR && result[1].data == listOf<Article>())
        }
    }

    @Test
    fun test_bookmark_articles() {
        runBlocking {
            val articleId = "1000AAL"
            val article = ArticleEntity(articleId, "", "", "", "", "", "", "", Date())

            val sut = ArticlesRepositoryImpl(webService, db, dataMapper, context)
            db.articlesDao().insertArticles(article)

            sut.bookmarkArticle(articleId)

            val bookmarkedArticleIds = context.dataStore.data
                .first()[ArticlesRepositoryImpl.BOOKMARKED_IDS_PREF_KEY]
                .orEmpty()
                .toList()

            val articles = db.articlesDao().getArticlesByIds(bookmarkedArticleIds).first()
            assertTrue(articles.map { it.id }.contains(articleId))
        }
    }

    @Test
    fun test_bookmark_articles_removes_existing_article_if_it_exists() {
        runBlocking {
            val articleId = "1000AAL"
            val article = ArticleEntity(articleId, "", "", "", "", "", "", "", Date())

            val sut = ArticlesRepositoryImpl(webService, db, dataMapper, context)
            db.articlesDao().insertArticles(article)

            sut.bookmarkArticle(articleId)
            sut.bookmarkArticle(articleId)

            val bookmarkedArticleIds = context.dataStore.data
                .first()[ArticlesRepositoryImpl.BOOKMARKED_IDS_PREF_KEY]
                .orEmpty()
                .toList()

            val articles = db.articlesDao().getArticlesByIds(bookmarkedArticleIds).first()
            assertTrue(articles.map { it.id }.contains(articleId))
        }
    }

    @Test
    fun test_refresh_articles_does_not_remove_bookmarked_articles_from_db() {
        runBlocking {
            val articleId = "1000AALI"
            val article = ArticleEntity(articleId, "", "", "", "", "", "", "", Date())

            val sut = ArticlesRepositoryImpl(webService, db, dataMapper, context)
            db.articlesDao().insertArticles(article)

            sut.bookmarkArticle(articleId)

            sut.refreshArticles(0)

            val dbArticles = db.articlesDao().getArticles().first()

            assertTrue(dbArticles.isEmpty().not())
            assertTrue(dbArticles[0].removed)

        }
    }

    @Test
    fun test_refresh_articles_removes_articles_from_db_when_size_is_greater_than_max_size() {
        runBlocking {
            val articleId = "1000AALI"
            val article = ArticleEntity(articleId, "", "", "", "", "", "", "", Date())

            val sut = ArticlesRepositoryImpl(webService, db, dataMapper, context)
            db.articlesDao().insertArticles(article)

            sut.refreshArticles(0)

            val dbArticles = db.articlesDao().getArticles().first()

            assertTrue(dbArticles.isEmpty())
        }
    }


    @After
    @Throws(IOException::class)
    fun after() {
        db.close()
        runBlocking { context.dataStore.edit { it.clear() } }
    }
}