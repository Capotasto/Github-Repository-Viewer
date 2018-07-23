package com.funckyhacker.githubrepoviewer.di

import com.funckyhacker.githubrepoviewer.App
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AndroidSupportInjectionModule::class),
    (AppModule::class),
    (MainModule::class),
    (NetworkModule::class),
    (ViewModelModule::class)
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
