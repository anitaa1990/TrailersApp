package com.an.trailers.ui.search.activity

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.SnapHelper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.an.trailers.AppConstants.Companion.TRANSITION_IMAGE_NAME
import com.an.trailers.R
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.databinding.SearchActivityBinding
import com.an.trailers.factory.ViewModelFactory
import com.an.trailers.ui.base.BaseActivity
import com.an.trailers.ui.base.custom.recyclerview.PagerSnapHelper
import com.an.trailers.ui.base.custom.recyclerview.RecyclerItemClickListener
import com.an.trailers.ui.base.custom.recyclerview.RecyclerSnapItemListener
import com.an.trailers.ui.search.adapter.MovieSearchListAdapter
import com.an.trailers.ui.search.viewmodel.MovieSearchViewModel
import com.an.trailers.utils.AppUtils
import com.an.trailers.utils.NavigationUtils
import dagger.android.AndroidInjection

import javax.inject.Inject

class MovieSearchActivity : BaseActivity(), SearchView.OnQueryTextListener,
    RecyclerItemClickListener.OnRecyclerViewItemClickListener {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private lateinit var searchViewModel: MovieSearchViewModel
    private lateinit var binding: SearchActivityBinding
    private lateinit var searchListAdapter: MovieSearchListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        initialiseView()
        initialiseViewModel()
    }

    private fun initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.search.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.search.imeOptions = EditorInfo.IME_ACTION_SEARCH
        binding.search.setIconifiedByDefault(false)
        binding.search.setOnQueryTextListener(this)

        val searchEditText = binding.search.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(resources.getColor(android.R.color.white))
        searchEditText.setHintTextColor(resources.getColor(android.R.color.white))
        val myCustomFont = ResourcesCompat.getFont(applicationContext, R.font.gt_medium)
        searchEditText.typeface = myCustomFont

        searchListAdapter = MovieSearchListAdapter(this)
        binding.includedLayout.moviesList.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.includedLayout.moviesList.adapter = searchListAdapter
        val startSnapHelper = PagerSnapHelper( object : RecyclerSnapItemListener {
            override fun onItemSnap(position: Int) {
                val trailer = searchListAdapter.getItem(position)
                updateBackground(trailer.getFormattedPosterPath())
            }
        })
        startSnapHelper.attachToRecyclerView(binding.includedLayout.moviesList)
        binding.includedLayout.moviesList.addOnItemTouchListener(RecyclerItemClickListener(applicationContext, this))
    }

    private fun initialiseViewModel() {
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieSearchViewModel::class.java)
    }

    private fun updateBackground(url: String?) {
        binding.overlayLayout.updateCurrentBackground(url)
    }

    private fun querySearch(text: String) {
        searchViewModel.searchMovie(text)
        searchViewModel.getMoviesLiveData().observe(this, Observer { resource ->
            if (resource!!.isLoading) {
                displayLoader()

            } else if (resource.data != null && !resource.data.isEmpty()) {
                handleSuccessResponse(resource.data)

            } else handleErrorResponse()
        })
    }

    private fun handleSuccessResponse(movies: List<MovieEntity>) {
        hideLoader()
        binding.includedLayout.emptyLayout.emptyContainer.visibility = View.GONE
        binding.includedLayout.moviesList.visibility = View.VISIBLE
        searchListAdapter.items = movies
        Handler().postDelayed({
            if (searchListAdapter.itemCount > 0) {
                updateBackground(searchListAdapter.getItem(0).getFormattedPosterPath())
            }

        }, 400)
    }

    private fun handleErrorResponse() {
        hideLoader()
        binding.includedLayout.moviesList.visibility = View.GONE
        binding.includedLayout.emptyLayout.emptyContainer.visibility = View.VISIBLE
    }


    private fun displayLoader() {
        binding.includedLayout.moviesList.visibility = View.GONE
        binding.includedLayout.loaderLayout.rootView.visibility = View.VISIBLE
        binding.includedLayout.loaderLayout.loader.start()
    }

    private fun hideLoader() {
        binding.includedLayout.moviesList.visibility = View.VISIBLE
        binding.includedLayout.loaderLayout.rootView.visibility = View.GONE
        binding.includedLayout.loaderLayout.loader.stop()
    }

    override fun onItemClick(parentView: View, childView: View, position: Int) {
        searchViewModel.getMoviesLiveData().removeObservers(this)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this, Pair(childView.findViewById(R.id.image), TRANSITION_IMAGE_NAME))

        NavigationUtils.redirectToDetailScreen(
            this, searchListAdapter.getItem(position),
            options)
    }

    override fun onQueryTextSubmit(s: String): Boolean {
        AppUtils.closeKeyboard(this)
        querySearch(s)
        return true
    }

    override fun onQueryTextChange(s: String): Boolean {
        return false
    }
}
