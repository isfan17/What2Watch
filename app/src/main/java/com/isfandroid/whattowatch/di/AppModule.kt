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
import com.isfandroid.whattowatch.feature.detail.DetailViewModel
import com.isfandroid.whattowatch.feature.home.HomeViewModel
import com.isfandroid.whattowatch.feature.list.ListViewModel
import com.isfandroid.whattowatch.feature.search.SearchViewModel
import com.isfandroid.whattowatch.feature.watchlist.WatchlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<HomeUseCase> { HomeInteractor(get()) }
    factory<SearchUseCase> { SearchInteractor(get()) }
    factory<WatchlistUseCase> { WatchlistInteractor(get()) }
    factory<DetailUseCase> { DetailInteractor(get()) }
    factory<ListUseCase> { ListInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { WatchlistViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { ListViewModel(get()) }
}