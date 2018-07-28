package com.funckyhacker.githubrepoviewer.data.repository

import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import io.reactivex.Single

interface GithubRepository {
    fun getRepos(org: String, page: Int): Single<List<Repository>>
}
