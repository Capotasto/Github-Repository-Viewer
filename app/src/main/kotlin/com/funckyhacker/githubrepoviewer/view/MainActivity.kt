package com.funckyhacker.githubrepoviewer.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.funckyhacker.githubrepoviewer.R
import com.funckyhacker.githubrepoviewer.data.repository.GithubRepository
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var repository: GithubRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)

        repository.getRepos("mixi-inc")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {Timber.i(it.toString())},
                        onError = {Timber.e(it)}
                )
    }
}
