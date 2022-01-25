package com.idrisadetunmbi.newsapp.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    val author: String,

    val content: String,

    val description: String,

    val source: String,

    val title: String,

    val url: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "published_at")
    val publishedAt: Date,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    val removed: Boolean = false
)
