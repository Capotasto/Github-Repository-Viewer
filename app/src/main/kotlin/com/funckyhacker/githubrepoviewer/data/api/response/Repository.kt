package com.funckyhacker.githubrepoviewer.data.api.response

import com.funckyhacker.githubrepoviewer.data.db.model.RepositoryEntity
import com.google.gson.annotations.SerializedName

data class Repository(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
) {
    companion object {
        fun copy(entity: RepositoryEntity): Repository  {
            return Repository(entity.id, entity.name)
        }
    }
}
