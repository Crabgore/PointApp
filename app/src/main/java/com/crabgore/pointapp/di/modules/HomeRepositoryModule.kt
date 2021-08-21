package com.crabgore.pointapp.di.modules

import com.crabgore.pointapp.data.GitRepositoryImpl
import com.crabgore.pointapp.data.GitService
import com.crabgore.pointapp.domain.repositories.GitRepository
import org.koin.dsl.module

val homeRepositoryModule =
    module {
        single { provideGitRepository(get()) }
    }

fun provideGitRepository(api: GitService): GitRepository {
    return GitRepositoryImpl(api)
}