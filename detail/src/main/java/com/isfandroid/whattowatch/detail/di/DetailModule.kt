package com.isfandroid.whattowatch.detail.di

import com.isfandroid.whattowatch.detail.ui.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { DetailViewModel(get()) }
}