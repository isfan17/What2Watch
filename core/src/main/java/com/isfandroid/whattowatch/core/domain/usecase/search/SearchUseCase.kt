package com.isfandroid.whattowatch.core.domain.usecase.search

import androidx.paging.PagingData
import com.isfandroid.whattowatch.core.data.source.remote.response.general.MultiResponse
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {
    fun searchMulti(query: String): Flow<PagingData<MultiResponse>>
}