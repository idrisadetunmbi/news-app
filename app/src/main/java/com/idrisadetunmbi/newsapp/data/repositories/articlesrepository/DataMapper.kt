package com.idrisadetunmbi.newsapp.data.repositories.articlesrepository

import com.idrisadetunmbi.newsapp.data.database.entities.ArticleEntity
import com.idrisadetunmbi.newsapp.data.repositories.articlesrepository.remote.AllArticlesResponseModel
import com.idrisadetunmbi.newsapp.models.Article
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DataMapper @Inject constructor() {
    fun mapArticle(src: ArticleEntity): Article {
        return with(src) {
            Article(
                id,
                author,
                content,
                description,
                source,
                title,
                url,
                publishedAt,
                imageUrl
            )
        }
    }

    fun mapToEntity(src: AllArticlesResponseModel.Article): ArticleEntity? {
        with(src) {
            val publishedAt = try {
                SimpleDateFormat(PUBLISHED_AT_DATE_PATTERN, Locale.getDefault()).parse(publishedAt)
            } catch (ex: ParseException) {
                null
            } ?: return null
            return ArticleEntity(
                this.publishedAt + title,
                author.orEmpty(),
                content.orEmpty(),
                description.orEmpty(),
                this.source.name.orEmpty(),
                title.orEmpty(),
                url.orEmpty(),
                urlToImage.orEmpty(),
                publishedAt
            )
        }
    }

    companion object {
        private const val PUBLISHED_AT_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }
}
