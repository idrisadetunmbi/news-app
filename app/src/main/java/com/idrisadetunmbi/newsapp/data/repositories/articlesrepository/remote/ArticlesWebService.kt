package com.idrisadetunmbi.newsapp.data.repositories.articlesrepository.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesWebService {
    @GET("top-headlines?country=us")
    suspend fun getAllArticles(): AllArticlesResponseModel
}
