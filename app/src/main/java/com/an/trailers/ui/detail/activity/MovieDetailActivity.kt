package com.an.trailers.ui.detail.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.an.trailers.AppConstants.Companion.CREDIT_CREW
import com.an.trailers.AppConstants.Companion.INTENT_MOVIE
import com.an.trailers.AppConstants.Companion.TRANSITION_IMAGE_NAME
import com.an.trailers.R
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.remote.model.Cast
import com.an.trailers.data.remote.model.Crew
import com.an.trailers.data.remote.model.Video
import com.an.trailers.databinding.DetailActivityBinding
import com.an.trailers.factory.ViewModelFactory
import com.an.trailers.ui.base.BaseActivity
import com.an.trailers.ui.base.custom.recyclerview.RecyclerItemClickListener
import com.an.trailers.ui.detail.adapter.CreditListAdapter
import com.an.trailers.ui.detail.adapter.SimilarMoviesListAdapter
import com.an.trailers.ui.detail.adapter.VideoListAdapter
import com.an.trailers.ui.detail.viewmodel.MovieDetailViewModel
import com.an.trailers.utils.AppUtils
import com.an.trailers.utils.NavigationUtils
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection

import javax.inject.Inject
import java.util.Arrays

class MovieDetailActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: DetailActivityBinding
    private lateinit var movieDetailViewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        initialiseView()
        initialiseViewModel()
    }

    private fun initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val movie = intent.getParcelableExtra<MovieEntity>(INTENT_MOVIE)
        Picasso.get().load(movie.getFormattedPosterPath()).into(binding.image)
        ViewCompat.setTransitionName(binding.image, TRANSITION_IMAGE_NAME)
        binding.expandButton.paintFlags = binding.expandButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG


    }

    private fun initialiseViewModel() {
        movieDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
        movieDetailViewModel.fetchMovieDetail(intent.getParcelableExtra(INTENT_MOVIE))
        movieDetailViewModel.getMovieDetailsLiveData().observe(this, Observer { movieEntity ->
            updateMovieDetailView(movieEntity!!)
            if (movieEntity.videos != null && !movieEntity.videos!!.isEmpty()) {
                updateMovieVideos(movieEntity.videos!!)
            }
            if (movieEntity.crews != null && !movieEntity.crews!!.isEmpty()) {
                updateMovieCrewDetails(movieEntity.crews!!)
            }

            if (movieEntity.casts != null && !movieEntity.casts!!.isEmpty()) {
                binding.expandButton.visibility = View.VISIBLE
                updateMovieCastDetails(movieEntity.casts!!)
            }
            if (movieEntity.similarMovies != null && !movieEntity.similarMovies!!.isEmpty()) {
                updateSimilarMoviesView(movieEntity.similarMovies!!)
            }
        })
    }


    private fun updateMovieDetailView(movie: MovieEntity) {
        binding.movieTitle.text = movie.header
        binding.movieDesc.text = movie.description
        if (movie.status != null) binding.movieStatus.items = Arrays.asList(movie.status)
        binding.collectionItemPicker.isUseRandomColor = true
        if (movie.genres != null) binding.collectionItemPicker.items = AppUtils.getGenres(movie.genres!!)
        binding.txtRuntime.text = AppUtils.getRunTimeInMins(movie.status, movie.runTime, movie.releaseDate)
    }

    private fun updateMovieVideos(videos: List<Video>) {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.smoothScrollToPosition(1)

        val videoListAdapter = VideoListAdapter(applicationContext, videos)
        binding.recyclerView.adapter = videoListAdapter
        binding.recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this, object : RecyclerItemClickListener.OnRecyclerViewItemClickListener {
                    override fun onItemClick(parentView: View, childView: View, position: Int) {
                        NavigationUtils.redirectToVideoScreen(
                            applicationContext, videoListAdapter.getItem(position).key
                        )
                    }
                })
        )
    }

    private fun updateMovieCastDetails(casts: List<Cast>) {
        binding.includedLayout.castList.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.includedLayout.castList.visibility = View.VISIBLE
        val creditListAdapter = CreditListAdapter(applicationContext, casts)
        binding.includedLayout.castList.adapter = creditListAdapter
    }

    private fun updateMovieCrewDetails(crews: List<Crew>) {
        binding.includedLayout.crewList.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.includedLayout.castList.visibility = View.VISIBLE
        val creditListAdapter = CreditListAdapter(applicationContext, CREDIT_CREW, crews)
        binding.includedLayout.crewList.adapter = creditListAdapter
    }

    private fun updateSimilarMoviesView(movies: List<MovieEntity>) {
        binding.includedSimilarLayout.moviesList.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.includedSimilarLayout.moviesList.visibility = View.VISIBLE
        val similarMoviesListAdapter = SimilarMoviesListAdapter(this, movies)
        binding.includedSimilarLayout.moviesList.adapter = similarMoviesListAdapter

        binding.includedSimilarLayout.moviesList.addOnItemTouchListener(
                RecyclerItemClickListener(applicationContext,
                        object : RecyclerItemClickListener.OnRecyclerViewItemClickListener {
                            override fun onItemClick(parentView: View, childView: View, position: Int) {
                                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        this@MovieDetailActivity, childView, TRANSITION_IMAGE_NAME)

                                NavigationUtils.redirectToDetailScreen(
                                        this@MovieDetailActivity, similarMoviesListAdapter.getItem(position), options)
                            }
                        }))
        binding.includedSimilarLayout.movieSimilarTitle.visibility = View.VISIBLE
    }


    fun handleExpandAction(view: View) {
        if (binding.includedLayout.expandableLayout.isExpanded) {
            binding.expandButton.text = getString(R.string.read_more)
            binding.includedLayout.expandableLayout.collapse()
        } else {
            binding.expandButton.text = getString(R.string.read_less)
            binding.includedLayout.expandableLayout.expand()
        }
    }
}
