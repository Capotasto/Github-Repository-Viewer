package com.funckyhacker.githubrepoviewer.di

import com.funckyhacker.githubrepoviewer.data.api.GithubApi
import com.funckyhacker.githubrepoviewer.data.repository.GithubDataRepository
import com.funckyhacker.githubrepoviewer.data.repository.GithubRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [
    (MainComponent::class)
])
class AppModule {
    @Singleton
    @Provides
    fun provideGithubRepository(api: GithubApi) : GithubRepository {
        return GithubDataRepository(api)
    }

}
