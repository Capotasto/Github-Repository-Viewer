package com.funckyhacker.githubrepoviewer.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.funckyhacker.githubrepoviewer.R
import com.funckyhacker.githubrepoviewer.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private val adapter: RepoListAdapter by lazy {
        RepoListAdapter()
    }

    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val decoration: DividerItemDecoration by lazy {
        DividerItemDecoration(this, layoutManager.orientation)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setSupportActionBar(binding.toolbar)
        setTitle(R.string.app_name)
        lifecycle.addObserver(viewModel)
        initRecycler()
        binding.apply {
            setLifecycleOwner(this@MainActivity)
            viewModel = this@MainActivity.viewModel
        }

        viewModel.repos.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun initRecycler() {
        binding.recyclerView.apply {
            layoutManager = this@MainActivity.layoutManager
            adapter = this@MainActivity.adapter
            addItemDecoration(decoration)
        }
    }
}
