package com.idrisadetunmbi.newsapp.ui.articledetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idrisadetunmbi.newsapp.core.data.Resource
import com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.ArticlesRepository
import com.idrisadetunmbi.newsapp.core.data.wrapAsResource
import com.idrisadetunmbi.newsapp.core.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ArticlesRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    init {
        savedStateHandle.get<String>("articleId")
            ?.let { articleId: String ->
                repository.getArticle(articleId)
                    .wrapAsResource()
                    .onEach { articlesRes -> _state.update { it.copy(articleRes = articlesRes) } }
                    .launchIn(viewModelScope)
            }
    }

    data class State(val articleRes: Resource<Article> = Resource.initial())
}