package com.isfandroid.whattowatch.di

import com.isfandroid.whattowatch.core.domain.usecase.detail.DetailInteractor
import com.isfandroid.whattowatch.core.domain.usecase.detail.DetailUseCase
import com.isfandroid.whattowatch.core.domain.usecase.home.HomeInteractor
import com.isfandroid.whattowatch.core.domain.usecase.home.HomeUseCase
import com.isfandroid.whattowatch.core.domain.usecase.list.ListInteractor
import com.isfandroid.whattowatch.core.domain.usecase.list.ListUseCase
import com.isfandroid.whattowatch.core.domain.usecase.search.SearchInteractor
import com.isfandroid.whattowatch.core.domain.usecase.search.SearchUseCase
import com.isfandroid.whattowatch.core.domain.usecase.watchlist.WatchlistInteractor
import com.isfandroid.whattowatch.core.domain.usecase.watchlist.WatchlistUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<HomeUseCase> { HomeInteractor(get()) }
    factory<SearchUseCase> { SearchInteractor(get()) }
    factory<WatchlistUseCase> { WatchlistInteractor(get()) }
    factory<DetailUseCase> { DetailInteractor(get()) }
    factory<ListUseCase> { ListInteractor(get()) }
}