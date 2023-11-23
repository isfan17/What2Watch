package com.isfandroid.whattowatch.watchlist.di

import com.isfandroid.whattowatch.watchlist.ui.WatchlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val watchlistModule = module {
    viewModel { WatchlistViewModel(get()) }
}