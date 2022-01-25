package com.idrisadetunmbi.newsapp.di

import android.content.Context
import androidx.room.Room
import com.idrisadetunmbi.newsapp.core.data.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, DB_NAME)
            .addMigrations()
            .fallbackToDestructiveMigration()
            .build()
    }

    private const val DB_NAME = "news-app"
}
