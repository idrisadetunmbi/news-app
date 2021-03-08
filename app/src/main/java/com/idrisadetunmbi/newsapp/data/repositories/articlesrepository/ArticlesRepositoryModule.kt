package com.idrisadetunmbi.newsapp.data.repositories.articlesrepository

import com.idrisadetunmbi.newsapp.data.repositories.articlesrepository.remote.ArticlesWebService
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
}

@Module
@InstallIn(SingletonComponent::class)
interface Bindings {
    @Binds
    @Singleton
    fun bindRepository(impl: ArticlesRepositoryImpl): ArticlesRepository
}
