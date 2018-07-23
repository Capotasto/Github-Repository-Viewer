package com.funckyhacker.githubrepoviewer.data.repository

import com.funckyhacker.githubrepoviewer.data.api.GithubApi
import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import io.reactivex.Single

class GithubDataRepository(private val api: GithubApi) : GithubRepository {
    override fun getRepos(org: String): Single<List<Repository>> {
        return api.getRepos(org)
    }
}
