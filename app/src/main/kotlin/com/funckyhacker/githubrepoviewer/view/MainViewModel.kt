package com.funckyhacker.githubrepoviewer.view

import android.arch.lifecycle.*
import android.databinding.ObservableField
import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import com.funckyhacker.githubrepoviewer.data.db.RepositoryDb
import com.funckyhacker.githubrepoviewer.data.repository.GithubRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val repository: GithubRepository,
        private val db: RepositoryDb
) : ViewModel(), LifecycleObserver {

    companion object {
        private const val ORG_NAME = "mixi-inc"
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repos = MutableLiveData<List<Repository>>()
    val isLoading = ObservableField<Boolean>(false)
    val showToast = MutableLiveData<Boolean>()
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
        callDb()
    }

    /**
     * 現在のページ数でデータベースを検索する
     */
    fun callDb() {
        Timber.i("callDb: page: %d", page)
        db.getAll(page)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{ isLoading.set(true) }
                .doFinally{ isLoading.set(false) }
                .subscribeBy(
                        onSuccess = {
                            Timber.d("size: %s", it.size)
                            if (it.isEmpty()) {
                                callRepoApi()
                                return@subscribeBy
                            }
                            // 初回の取得のケース
                            if (repos.value == null) {
                                repos.postValue(it)
                                page++
                                return@subscribeBy
                            }
                            // 上記以外のケース
                            val list = ArrayList(repos.value)
                            list.addAll(it)
                            repos.postValue(list)
                            page++

                        },
                        onError = {
                            Timber.w(it)
                        }
                )
                .addTo(compositeDisposable)
    }

    /**
     * Github Repository API 呼び出し
     */
    fun callRepoApi() {
        Timber.i("callRepoApi: page: %d", page)
        repository.getRepos(ORG_NAME, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{ isLoading.set(true) }
                .doFinally { isLoading.set(false) }
                .subscribeBy(
                        onSuccess = {
                            // 最終ページのケース
                            if (it.isEmpty()) {
                                isLastPage = true
                                return@subscribeBy
                            }
                            // 初回の取得のケース
                            if (repos.value == null) {
                                repos.postValue(it)
                                saveToDb(it, page)
                                page++
                                return@subscribeBy
                            }
                            // 上記以外のケース
                            val list = ArrayList(repos.value)
                            list.addAll(it)
                            repos.postValue(list)
                            saveToDb(it, page)
                            page++
                        },
                        onError =  {
                            Timber.w(it)
                            showToast.postValue(true)
                        }
                ).addTo(compositeDisposable)
    }

    /**
     * DBへの保存処理
     */
    private fun saveToDb(list: List<Repository>, page: Int) {
        db.save(list, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            Timber.i("Saved %d record(s) as page %d", list.size, page)
                        },
                        onError = { Timber.w(it) }
                ).addTo(compositeDisposable)

    }
}
