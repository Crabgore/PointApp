package com.crabgore.pointapp.data

import com.crabgore.pointapp.Const.MyPreferences.Companion.HEADER
import com.crabgore.pointapp.domain.repositories.GitRepository
import io.reactivex.Observable

class GitRepositoryImpl(
    private val api: GitService
): GitRepository {
    override fun searchUser(q: String?, page: Int?): Observable<SearchResponse> {
        return api.searchUser(HEADER, q, page)
    }
}