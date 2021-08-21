package com.crabgore.pointapp.di.modules

import com.crabgore.pointapp.Const.MyPreferences.Companion.BASE_URL
import com.crabgore.pointapp.data.GitService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val service: Retrofit =
    Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

val gitHubModule =
    module {
        single { service.create(GitService::class.java) }
    }