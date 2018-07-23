package com.funckyhacker.githubrepoviewer.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.funckyhacker.githubrepoviewer.R
import com.funckyhacker.githubrepoviewer.data.api.response.Repository
import com.funckyhacker.githubrepoviewer.databinding.ItemRepoBinding

class RepoListAdapter : RecyclerView.Adapter<RepoListAdapter.ViewHolder>() {

    var repos: List<Repository> = arrayListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemRepoBinding>(LayoutInflater.from(parent.context),
                R.layout.item_repo, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.repo = repos[position]
    }

    inner class ViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)
}
