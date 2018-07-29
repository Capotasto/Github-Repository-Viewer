package com.funckyhacker.githubrepoviewer.di

import com.funckyhacker.githubrepoviewer.data.db.RepositoryDb
import com.funckyhacker.githubrepoviewer.data.db.RepositoryRealmDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRepositoryDb(): RepositoryDb {
        return RepositoryRealmDb()
    }
}
