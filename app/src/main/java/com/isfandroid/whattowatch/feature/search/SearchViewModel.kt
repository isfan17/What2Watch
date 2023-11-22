package com.isfandroid.whattowatch.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.isfandroid.whattowatch.core.data.source.remote.response.general.MultiResponse
import com.isfandroid.whattowatch.core.domain.usecase.search.SearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val useCase: SearchUseCase): ViewModel() {

    private val _searchResults = MutableStateFlow<PagingData<MultiResponse>?>(null)
    val searchResults: StateFlow<PagingData<MultiResponse>?> = _searchResults

    fun searchMulti(query: String) {
        viewModelScope.launch {
            useCase.searchMulti(query).cachedIn(viewModelScope).collect {
                _searchResults.value = it
            }
        }
    }
}