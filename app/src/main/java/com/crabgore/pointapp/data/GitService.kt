package com.crabgore.pointapp.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GitService {

    @GET("search/users")
    fun searchUser(
        @Header("accept") accept: String,
        @Query("q") q: String?,
        @Query("page") page: Int?
    ): Observable<SearchResponse>
}