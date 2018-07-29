package com.funckyhacker.githubrepoviewer.data.db.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RepositoryEntity(
        @PrimaryKey var id: Int = 0,
        var page: Int = 0,
        var name: String = ""
): RealmObject()
