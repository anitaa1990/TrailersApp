package com.an.trailers.ui.main.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.an.trailers.AppConstants.Companion.INTENT_CATEGORY
import com.an.trailers.AppConstants.Companion.MENU_MOVIE_ITEM
import com.an.trailers.AppConstants.Companion.TRANSITION_IMAGE_NAME
import com.an.trailers.R
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.databinding.MoviesListFragmentBinding
import com.an.trailers.factory.ViewModelFactory
import com.an.trailers.ui.base.BaseFragment
import com.an.trailers.ui.base.custom.recyclerview.PagerSnapHelper
import com.an.trailers.ui.base.custom.recyclerview.RecyclerItemClickListener
import com.an.trailers.ui.base.custom.recyclerview.RecyclerSnapItemListener
import com.an.trailers.ui.main.activity.MainActivity
import com.an.trailers.ui.main.adapter.MoviesListAdapter
import com.an.trailers.ui.main.viewmodel.MovieListViewModel
import com.an.trailers.utils.NavigationUtils
import dagger.android.support.AndroidSupportInjection

import javax.inject.Inject

class MovieListFragment : BaseFragment(), RecyclerItemClickListener.OnRecyclerViewItemClickListener {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: MoviesListFragmentBinding
    private lateinit var moviesListAdapter: MoviesListAdapter
    lateinit var moviesListViewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        initialiseViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseView()
    }

    private fun initialiseView() {
        moviesListAdapter = MoviesListAdapter(activity)
        binding.moviesList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.moviesList.adapter = moviesListAdapter
        val startSnapHelper = PagerSnapHelper(
            object : RecyclerSnapItemListener {
                override fun onItemSnap(position: Int) {
                    val movie = moviesListAdapter.getItem(position)
                    (activity as MainActivity).updateBackground(movie.getFormattedPosterPath())
                }
            }
        )
        startSnapHelper.attachToRecyclerView(binding.moviesList)
        binding.moviesList.addOnItemTouchListener(RecyclerItemClickListener(requireContext(), this))
    }


    private fun initialiseViewModel() {
        moviesListViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel::class.java)
        moviesListViewModel.fetchMovies(MENU_MOVIE_ITEM[if (arguments == null) 0 else arguments!!.getInt(INTENT_CATEGORY)]!!
        )
        moviesListViewModel.getMoviesLiveData().observe(this, Observer { resource ->
            if (resource!!.isLoading) {
                displayLoader()

            } else if (resource.data != null && !resource.data.isEmpty()) {
                handleSuccessResponse(resource.data)

            } else
                handleErrorResponse()
        })
    }

    private fun handleSuccessResponse(movies: List<MovieEntity>) {
        hideLoader()
        moviesListViewModel.getMoviesLiveData().removeObservers(this)
        binding.emptyLayout.emptyContainer.visibility = View.GONE
        binding.moviesList.visibility = View.VISIBLE
        moviesListAdapter.setItems(movies)
        Handler().postDelayed({
            if (moviesListAdapter.itemCount > 0) {
                (activity as MainActivity).updateBackground(moviesListAdapter.getItem(0).getFormattedPosterPath())
            }

        }, 400)
    }

    private fun handleErrorResponse() {
        hideLoader()
        binding.moviesList.visibility = View.GONE
        binding.emptyLayout.emptyContainer.visibility = View.VISIBLE
        (activity as MainActivity).clearBackground()
    }


    private fun displayLoader() {
        binding.moviesList.visibility = View.GONE
        binding.loaderLayout.rootView.visibility = View.VISIBLE
        binding.loaderLayout.loader.start()
        (activity as MainActivity).hideToolbar()
    }

    private fun hideLoader() {
        binding.moviesList.visibility = View.VISIBLE
        binding.loaderLayout.rootView.visibility = View.GONE
        binding.loaderLayout.loader.stop()
        (activity as MainActivity).displayToolbar()
    }

    override fun onItemClick(parentView: View, childView: View, position: Int) {
        moviesListViewModel.getMoviesLiveData().removeObservers(this)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, Pair(childView.findViewById(R.id.image), TRANSITION_IMAGE_NAME))

        NavigationUtils.redirectToDetailScreen(
            activity, moviesListAdapter.getItem(position), options
        )
    }
}