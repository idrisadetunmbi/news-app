package com.idrisadetunmbi.newsapp.ui.savedarticles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.ArticlesRepository
import com.idrisadetunmbi.newsapp.core.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SavedArticlesViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    internal val state: StateFlow<State> = _state

    init {
        articlesRepository
            .getBookmarkedArticleIds()
            .flatMapLatest { bookmarkedIds ->
                articlesRepository.getArticlesByIds(bookmarkedIds)
                    .map { it.map { article -> article.copy(bookmarked = true) } }
            }
            .onEach { _state.value = _state.value.copy(articles = it) }
            .launchIn(viewModelScope)
    }

    fun handleBookmarkArticle(article: Article) {
        viewModelScope.launch {
            articlesRepository.bookmarkArticle(article.id)
        }
    }

    internal data class State(val articles: List<Article> = listOf())
}
