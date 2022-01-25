package com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.remote

import retrofit2.http.GET

interface ArticlesWebService {
    @GET("top-headlines?country=us")
    suspend fun getAllArticles(): AllArticlesResponseModel
}
