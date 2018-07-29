package com.funckyhacker.githubrepoviewer.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.funckyhacker.githubrepoviewer.R
import com.funckyhacker.githubrepoviewer.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import timber.log.Timber
import java.util.*
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
        var currentTime = Date().time
        binding.recyclerView.apply {
            layoutManager = this@MainActivity.layoutManager
            adapter = this@MainActivity.adapter
            addItemDecoration(decoration)
            // Tricky: 通常のScrollListenerでは反応しすぎて重複して呼び出しが発生してしまう
            // そのため、前回イベント時刻と比較して 500[ms] 以上経ったもののみをトリガーする。
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val totalCount = recyclerView.adapter.itemCount//The number of item in adapter
                    val childCount = recyclerView.childCount //The number of item which is shown on RecyclerView
                    val firstPosition = this@MainActivity.layoutManager.findFirstVisibleItemPosition() // The fist position of RecyclerView
                    if (totalCount == childCount + firstPosition && isTriggeredEvent(currentTime, Date().time)) {
                        currentTime = Date().time
                        // Paging
                        Timber.i("Get Next Page")
                        viewModel.getRepos()
                    }
                }
            })
        }
    }

    private fun isTriggeredEvent(old: Long, new: Long): Boolean {
        return new - old >= 500 //
    }
}
