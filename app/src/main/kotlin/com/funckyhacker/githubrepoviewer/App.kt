package com.funckyhacker.githubrepoviewer

import com.facebook.stetho.Stetho
import com.funckyhacker.githubrepoviewer.di.DaggerAppComponent
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.realm.Realm
import timber.log.Timber


class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        // init Realm
        Realm.init(this)

        if (BuildConfig.DEBUG) {
            // Timber
            Timber.plant(Timber.DebugTree())
            // Stetho
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                            .build()
            )
        }
    }
}
