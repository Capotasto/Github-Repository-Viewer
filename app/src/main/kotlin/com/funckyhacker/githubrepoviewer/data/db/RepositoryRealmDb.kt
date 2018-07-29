package com.funckyhacker.githubrepoviewer.data.db

import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import com.funckyhacker.githubrepoviewer.data.db.model.RepositoryEntity
import io.reactivex.Completable
import io.reactivex.Single
import io.realm.Realm
import javax.inject.Singleton

@Singleton
class RepositoryRealmDb: RepositoryDb {

    /**
     * 指定のページ数で、Repositoryのリストをキャッシュする
     */
    override fun save(repos: List<Repository>, page: Int): Completable {
        return Completable.create { emitter ->
            val realm = Realm.getDefaultInstance()
            val repoEntityList = ArrayList<RepositoryEntity>()
            repos.forEach{repo ->
                repoEntityList.add(RepositoryEntity(repo.id, page, repo.name))
            }
            realm.executeTransactionAsync { realmInstance ->
                try {
                    realmInstance.insertOrUpdate(repoEntityList)
                    realmInstance.commitTransaction()
                } catch (e: Exception){
                    realmInstance.cancelTransaction()
                } finally {
                    emitter.onComplete()
                }
            }
        }
    }

    /**
     * 指定のページ番号に紐づく全レコードを非同期で取得して返却する
     */
    override fun getAll(page: Int): Single<List<Repository>> {
        return Single.create{ emitter ->
            val realm = Realm.getDefaultInstance()
            try {
              val realmResults = realm.where(RepositoryEntity::class.java)
                      .equalTo("page", page)
                      .findAllAsync()
                val listRepoEntity = realm.copyFromRealm(realmResults)
                val listRepos = convertToRepoList(listRepoEntity)
                emitter.onSuccess(listRepos);
            } catch (e: Exception) {
                emitter.onError(e)
            } finally {
                realm.close()
            }
        }
    }

    private fun convertToRepoList(list: List<RepositoryEntity>): List<Repository> {
        val results = ArrayList<Repository>()
        for (entity in list) {
            results.add(Repository.copy(entity))
        }
        return results
    }
}
