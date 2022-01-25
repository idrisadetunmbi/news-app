package com.idrisadetunmbi.newsapp.di

import com.idrisadetunmbi.newsapp.core.data.database.Database
import com.idrisadetunmbi.newsapp.core.data.database.daos.ArticlesDao
import com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.ArticlesRepository
import com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.ArticlesRepositoryImpl
import com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.remote.ArticlesWebService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Providers {
    @Provides
    @Singleton
    fun provideWebService(retrofit: Retrofit): ArticlesWebService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideArticlesDao(database: Database): ArticlesDao {
        return database.articlesDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface Bindings {
    @Binds
    @Singleton
    fun bindRepository(impl: ArticlesRepositoryImpl): ArticlesRepository
}
