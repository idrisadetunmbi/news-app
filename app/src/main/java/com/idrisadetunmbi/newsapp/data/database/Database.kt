package com.idrisadetunmbi.newsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.idrisadetunmbi.newsapp.data.database.daos.ArticlesDao
import com.idrisadetunmbi.newsapp.data.database.entities.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao
}
