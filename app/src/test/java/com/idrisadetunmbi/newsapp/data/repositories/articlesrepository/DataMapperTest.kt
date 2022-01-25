package com.idrisadetunmbi.newsapp.data.repositories.articlesrepository

import com.idrisadetunmbi.newsapp.core.data.database.entities.ArticleEntity
import com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.DataMapper
import com.idrisadetunmbi.newsapp.core.models.Article
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class DataMapperTest {
    @Test
    fun maps_Domain_Article_Correctly() {
        val publishedAt = Date()
        val input = ArticleEntity(
            "",
            "John Doe",
            "Lorem ipsum",
            "Lorem ipsum",
            "CNN",
            "Lorem ipsum sit amet",
            "",
            "",
            publishedAt
        )
        val expectedOutput = Article(
            "",
            "John Doe",
            "Lorem ipsum",
            "Lorem ipsum",
            "CNN",
            "Lorem ipsum sit amet",
            "",
            publishedAt,
            ""
        )
        val output = DataMapper().mapArticle(input)
        assertTrue(output == expectedOutput)
    }
}
