package com.idrisadetunmbi.newsapp.core.data.database.daos

import androidx.room.*
import com.idrisadetunmbi.newsapp.core.data.database.entities.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticles(vararg articles: ArticleEntity)

    @Query("SELECT * FROM articles ORDER BY created_at DESC")
    fun getArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE removed = 0 ORDER BY created_at DESC")
    fun getUnremovedArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id IN (:ids) ORDER BY created_at DESC")
    fun getArticlesByIds(ids: List<String>): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticle(id: String): Flow<ArticleEntity>

    @Delete
    suspend fun clearArticles(vararg articles: ArticleEntity)

    @Update
    suspend fun updateArticles(vararg articles: ArticleEntity)
}
