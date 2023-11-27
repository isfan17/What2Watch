package com.isfandroid.whattowatch.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.domain.usecase.search.SearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val useCase: SearchUseCase): ViewModel() {

    private val _searchResults = MutableStateFlow<PagingData<Multi>?>(null)
    val searchResults: StateFlow<PagingData<Multi>?> = _searchResults

    fun searchMulti(query: String) {
        viewModelScope.launch {
            useCase.searchMulti(query).cachedIn(viewModelScope).collect {
                _searchResults.value = it
            }
        }
    }
}