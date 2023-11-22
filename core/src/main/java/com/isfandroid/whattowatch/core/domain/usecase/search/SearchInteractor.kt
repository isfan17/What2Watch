package com.isfandroid.whattowatch.core.domain.usecase.search

import com.isfandroid.whattowatch.core.domain.repository.IAppRepository

class SearchInteractor(private val repository: IAppRepository): SearchUseCase {
    override fun searchMulti(query: String) = repository.searchMultiPaging(query)
}