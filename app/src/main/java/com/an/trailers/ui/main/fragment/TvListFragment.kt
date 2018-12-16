package com.an.trailers.ui.main.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.an.trailers.AppConstants.Companion.MENU_TV_ITEM
import com.an.trailers.AppConstants.Companion.TRANSITION_IMAGE_NAME
import com.an.trailers.R
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.databinding.MoviesListFragmentBinding
import com.an.trailers.factory.ViewModelFactory
import com.an.trailers.ui.base.BaseFragment
import com.an.trailers.ui.base.custom.recyclerview.PagerSnapHelper
import com.an.trailers.ui.base.custom.recyclerview.RecyclerItemClickListener
import com.an.trailers.ui.base.custom.recyclerview.RecyclerSnapItemListener
import com.an.trailers.ui.base.custom.recyclerview.RecyclerViewPaginator
import com.an.trailers.ui.main.activity.MainActivity
import com.an.trailers.ui.main.adapter.TvListAdapter
import com.an.trailers.ui.main.viewmodel.TvListViewModel
import com.an.trailers.utils.NavigationUtils
import dagger.android.support.AndroidSupportInjection

import javax.inject.Inject

class TvListFragment : BaseFragment(), RecyclerItemClickListener.OnRecyclerViewItemClickListener {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    private lateinit var tvListViewModel: TvListViewModel
    private lateinit var tvListAdapter: TvListAdapter
    private lateinit var binding: MoviesListFragmentBinding

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
        tvListAdapter = TvListAdapter(activity)
        binding.moviesList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.moviesList.adapter = tvListAdapter
        val startSnapHelper = PagerSnapHelper(
            object : RecyclerSnapItemListener {
                override fun onItemSnap(position: Int) {
                    val movie = tvListAdapter.getItem(position)
                    (activity as MainActivity).updateBackground(movie.getFormattedPosterPath())
                }
        })
        startSnapHelper.attachToRecyclerView(binding.moviesList)
        binding.moviesList.addOnItemTouchListener(RecyclerItemClickListener(requireContext(), this))

        binding.moviesList.addOnScrollListener(object : RecyclerViewPaginator(binding.moviesList) {
            override val isLastPage: Boolean
                get() = tvListViewModel.isLastPage()

            override fun loadMore(page: Long) {
                tvListViewModel.loadMoreTvs(page)
            }

            override fun loadFirstData(page: Long) {
                displayLoader()
                tvListViewModel.loadMoreTvs(page)
            }
        })
    }


    private fun initialiseViewModel() {
        tvListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TvListViewModel::class.java)
        tvListViewModel.setType(MENU_TV_ITEM.get(arguments!!.getInt(INTENT_CATEGORY))!!)
        tvListViewModel.getTvListLiveData().observe(this, Observer { resource ->
            if (resource!!.isLoading) {

            } else if (resource.data != null && !resource.data.isEmpty()) {
                updateTvsList(resource.data)

            } else
                handleErrorResponse()
        })
    }

    private fun updateTvsList(movies: List<TvEntity>) {
        hideLoader()
        binding.emptyLayout.emptyContainer.visibility = View.GONE
        binding.moviesList.visibility = View.VISIBLE
        tvListAdapter.setItems(movies)
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
        tvListViewModel.onStop()
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, Pair(childView.findViewById(R.id.image), TRANSITION_IMAGE_NAME))

        NavigationUtils.redirectToTvDetailScreen(
            activity, tvListAdapter.getItem(position),
            options)
    }
}
