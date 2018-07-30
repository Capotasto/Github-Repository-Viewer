package com.funckyhacker.githubrepoviewer.view

import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import com.funckyhacker.githubrepoviewer.data.db.RepositoryDb
import com.funckyhacker.githubrepoviewer.data.repository.GithubRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    @Mock
    private val repository: GithubRepository = mock()

    @Mock
    private val db: RepositoryDb = mock()

    private lateinit var viewModel: MainViewModel

    @Test
    fun shouldCallGetAllOnce() {
        // Given
        val mockRepos = mock<List<Repository>>()

        // When
        whenever(db.getAll(any())).doReturn(Single.just(mockRepos))
        viewModel = MainViewModel(repository, db)
        viewModel.callDb()

        // then
        verify(db).getAll(any())
    }


    @Test
    fun shouldCallGetReposOnce() {
        // Given
        val mockRepos = mock<List<Repository>>()

        // When
        whenever(repository.getRepos(any(), any())).doReturn(Single.just(mockRepos))
        viewModel = MainViewModel(repository, db)
        viewModel.callRepoApi()

        // then
        verify(repository).getRepos(any(), any())
    }

    //TODO:
    // 上記は実際はテストをする必要がないです。
    // 時間切れのためUnitテストのやり方は知っているのを見せるのが狙いです。
    // 実際にここでやるべきテストは下記となります。
    // - Mockを使ったDBの呼び出し後、onSuccessの場合とonErrorの場合でそれぞれの挙動を確認
    // - Mockを使ったAPIの呼び出し後、onSuccessの場合とonErrorの場合でそれぞれの挙動を確認
    //さらに、onSuccessには3通りのケースがあるため、それもすべてモックにて確認をする予定でした。

}


