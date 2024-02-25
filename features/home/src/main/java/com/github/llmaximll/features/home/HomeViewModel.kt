package com.github.llmaximll.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.llmaximll.core.common.Category
import com.github.llmaximll.core.common.di.IoDispatcher
import com.github.llmaximll.core.common.launchWithHandler
import com.github.llmaximll.core.common.models.Article
import com.github.llmaximll.core.data.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _searchFlow = MutableStateFlow("")
    val searchFlow: StateFlow<String> =
        _searchFlow.asStateFlow()

    private val _articlesState = MutableStateFlow<ArticlesState>(
        Category.entries.associateBy { it }.mapValues { (_, _) ->
            CategoryState.Init
        }
    )

    val articlesState: StateFlow<ArticlesState> = combine(
        _articlesState,
        _searchFlow
    ) { articlesState, searchQuery ->
        val newMap = articlesState.toMutableMap()
        articlesState.forEach { (category, state) ->
            if (state is CategoryState.Success) {
                val newArticles = state.articles.toMutableList()
                state.articles.forEach { article ->
                    if (article.title?.contains(searchFlow.value) != true &&
                        article.description?.contains(searchFlow.value) != true
                    ) {
                        newArticles.remove(article)
                    }
                }
                newMap[category] = CategoryState.Success(newArticles)
            }
        }
        newMap
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = mapOf()
    )

    init {
        Category.entries.forEach { category ->
            launchWithHandler(
                dispatcher = ioDispatcher,
                onException = {
                    changeArticlesState(category, CategoryState.Error(it))
                }
            ) {
                getTopHeadlinesByCategory(category)
            }
        }
    }

    private suspend fun getTopHeadlinesByCategory(category: Category) {
        changeArticlesState(category, CategoryState.Loading)

        val result = newsRepository.getTopHeadlinesByCategory(category = category)
        val data = result.getOrNull()

        if (result.isSuccess && !data.isNullOrEmpty()) {
            changeArticlesState(
                category,
                CategoryState.Success(data.sortedByDescending { it.publishedAt })
            )
        } else {
            changeArticlesState(category, CategoryState.Error(Throwable(result.exceptionOrNull())))
        }
    }

    private fun changeArticlesState(category: Category, newState: CategoryState) {
        val newMap = _articlesState.value.toMutableMap()
        newMap[category] = newState
        _articlesState.value = newMap
    }

    fun changeSearchText(newText: String) {
        _searchFlow.value = newText
    }
}

typealias ArticlesState = Map<Category, CategoryState>

sealed class CategoryState {

    data object Init : CategoryState()

    data object Loading : CategoryState()

    data class Error(val error: Throwable?) : CategoryState()

    data class Success(val articles: List<Article>) : CategoryState()
}