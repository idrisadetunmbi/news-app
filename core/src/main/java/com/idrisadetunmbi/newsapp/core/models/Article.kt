package com.idrisadetunmbi.newsapp.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Article(
    val id: String,
    val author: String,
    val content: String,
    val description: String,
    val source: String,
    val title: String,
    val url: String,
    val publishedAt: Date,
    val imageUrl: String,
    val bookmarked: Boolean = false
) : Parcelable

val Article.sourceDateText: String
    get() {
        val dateFormatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(publishedAt)
        return "${author}, $source - $dateFormatted"
    }
