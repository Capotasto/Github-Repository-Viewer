package com.funckyhacker.githubrepoviewer

import android.app.Application
import timber.log.Timber

class App : Application(){

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            // Timber
            Timber.plant(Timber.DebugTree())
        }
    }
}
