package com.crabgore.pointapp

import android.app.Application
import com.crabgore.pointapp.di.modules.gitHubModule
import com.crabgore.pointapp.di.modules.homeFragmentModule
import com.crabgore.pointapp.di.modules.homeRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * Сажаем Timber
         */
        Timber.plant(Timber.DebugTree())

        /**
         * Стартуем Koin
         */
        startKoin {

            //inject Android context
            androidContext(this@App)

            // declare modules
            modules(
                listOf(
                    homeRepositoryModule,
                    homeFragmentModule,
                    gitHubModule
                )
            )
        }
    }
}