package com.idrisadetunmbi.newsapp.data.repositories.articlesrepository.remote

data class AllArticlesResponseModel(
    val articles: List<Article> = listOf()
) {
    data class Article(
        val author: String? = "",
        val content: String? = "",
        val description: String? = "",
        val publishedAt: String = "",
        val source: Source = Source(),
        val title: String? = "",
        val url: String? = "",
        val urlToImage: String? = ""
    ) {
        data class Source(
            val id: String? = "",
            val name: String? = ""
        )
    }
}
