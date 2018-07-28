package com.funckyhacker.githubrepoviewer.data.api

import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface GithubApi {

    @GET("orgs/{org}/repos")
    fun getRepos(@Path("org") org: String, @Query("page") page: Int): Single<List<Repository>>
}
