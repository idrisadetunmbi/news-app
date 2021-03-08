package com.idrisadetunmbi.newsapp.ui.browsearticles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idrisadetunmbi.newsapp.data.Resource
import com.idrisadetunmbi.newsapp.data.repositories.articlesrepository.ArticlesRepository
import com.idrisadetunmbi.newsapp.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseArticlesViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    internal val state: StateFlow<State> = _state

    init {
        combine(
            articlesRepository.getArticles(),
            articlesRepository.getBookmarkedArticleIds()
        ) { articlesRes, bookmarkedArticles ->
            val updatedArticles = articlesRes.data
                ?.map { it.copy(bookmarked = bookmarkedArticles.contains(it.id)) }
            articlesRes.copy(data = updatedArticles)
        }
            .onEach { _state.value = _state.value.copy(articles = it) }
            .catch {
                // no-op
            }
            .launchIn(viewModelScope)
    }

    fun handleBookmarkArticle(article: Article) {
        viewModelScope.launch {
            articlesRepository.bookmarkArticle(article.id)
        }
    }

    fun handleRefreshArticles() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)
            try {
                articlesRepository.refreshArticles()
            } catch (ex: Exception) {
                // no-op
            } finally {
                _state.value = _state.value.copy(isRefreshing = false)
            }
        }
    }

    internal data class State(
        val articles: Resource<List<Article>> = Resource.loading(null),
        val isRefreshing: Boolean = false
    )
}
