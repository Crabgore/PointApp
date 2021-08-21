package com.crabgore.pointapp.domain.repositories

import com.crabgore.pointapp.data.SearchResponse
import io.reactivex.Observable
import org.koin.core.component.KoinComponent

interface GitRepository : KoinComponent {
    fun searchUser(q: String?, page: Int?): Observable<SearchResponse>
}