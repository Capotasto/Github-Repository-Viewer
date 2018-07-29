package com.funckyhacker.githubrepoviewer.data.db

import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import io.reactivex.Completable
import io.reactivex.Single

interface RepositoryDb {
    fun getAll(page: Int): Single<List<Repository>>
    fun save(repos: List<Repository>, page: Int) : Completable
}
