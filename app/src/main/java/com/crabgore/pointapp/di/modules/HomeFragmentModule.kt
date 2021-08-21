package com.crabgore.pointapp.di.modules

import com.crabgore.pointapp.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeFragmentModule =
    module {
        viewModel { HomeViewModel(get()) }
    }