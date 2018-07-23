package com.funckyhacker.githubrepoviewer.di

import android.app.Activity
import com.funckyhacker.githubrepoviewer.view.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindInjectorFactory(
            builder: MainComponent.Builder): AndroidInjector.Factory<out Activity>
}
