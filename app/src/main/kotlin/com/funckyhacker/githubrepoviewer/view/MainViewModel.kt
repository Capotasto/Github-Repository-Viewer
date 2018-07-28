package com.funckyhacker.githubrepoviewer.view

import android.arch.lifecycle.*
import android.databinding.ObservableField
import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import com.funckyhacker.githubrepoviewer.data.repository.GithubRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val repository: GithubRepository
) : ViewModel(), LifecycleObserver {

    companion object {
        private const val ORG_NAME = "mixi-inc"
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repos = MutableLiveData<List<Repository>>()
    val isLoading = ObservableField<Boolean>(false)
    private var page = 1
    private var isLastPage = false

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
        isLastPage = false
        page = 1
        getRepos()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared")
    }

    fun getRepos() {
        if (isLastPage || isLoading.get()!!) {
            return
        }
        repository.getRepos(ORG_NAME, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{ isLoading.set(true) }
                .doFinally { isLoading.set(false) }
                .subscribeBy(
                        onSuccess = {
                            if (it.isEmpty()) {
                                isLastPage = true
                                return@subscribeBy
                            }
                            if (repos.value == null) {
                                repos.postValue(it)
                                page++
                                return@subscribeBy
                            }
                            val list = ArrayList(repos.value)
                            list.addAll(it)
                            repos.postValue(list)
                            page++
                        },
                        onError =  {Timber.w(it)}
                ).addTo(compositeDisposable)
    }

}
