package com.funckyhacker.githubrepoviewer.view

import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.funckyhacker.githubrepoviewer.R
import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import com.funckyhacker.githubrepoviewer.databinding.ItemRepoBinding

class RepoListAdapter : ListAdapter<Repository, RepoListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemRepoBinding>(LayoutInflater.from(parent.context),
                R.layout.item_repo, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.repo = getItem(position)
    }

    inner class ViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Repository> = object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem == newItem
            }
        }
    }
}
