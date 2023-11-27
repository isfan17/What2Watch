package com.isfandroid.whattowatch.list.di

import com.isfandroid.whattowatch.list.ui.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listModule = module {
    viewModel { ListViewModel(get()) }
}