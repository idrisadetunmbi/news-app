package com.idrisadetunmbi.newsapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.idrisadetunmbi.newsapp.R
import com.idrisadetunmbi.newsapp.core.models.Article
import com.idrisadetunmbi.newsapp.core.models.sourceDateText

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticlesList(
    articles: List<Article>,
    onClickBookmarkItem: (Article) -> Unit,
    onClickItem: (Article) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        itemsIndexed(articles, { _, item -> item.id }) { _, item ->
            Card(modifier = Modifier.fillMaxSize(), onClick = { onClickItem(item) }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                ) {
                    Row {
                        Column(modifier = Modifier.weight(1F)) {
                            Text(
                                text = item.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = item.sourceDateText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Image(
                            painter = rememberImagePainter(item.imageUrl),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .size(56.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Text(
                            text = item.description,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1F),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(onClick = { onClickBookmarkItem(item) }) {
                            Icon(
                                painter = painterResource(id = if (item.bookmarked) R.drawable.ic_baseline_bookmark_24 else R.drawable.ic_outline_bookmark_border_24),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
