package com.an.trailers.ui.detail.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.an.trailers.AppConstants.Companion.CREDIT_CREW
import com.an.trailers.AppConstants.Companion.INTENT_MOVIE
import com.an.trailers.AppConstants.Companion.TRANSITION_IMAGE_NAME
import com.an.trailers.R
import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.data.remote.model.Cast
import com.an.trailers.data.remote.model.Crew
import com.an.trailers.data.remote.model.Video
import com.an.trailers.databinding.DetailActivityBinding
import com.an.trailers.factory.ViewModelFactory
import com.an.trailers.ui.base.BaseActivity
import com.an.trailers.ui.base.custom.recyclerview.RecyclerItemClickListener
import com.an.trailers.ui.detail.adapter.CreditListAdapter
import com.an.trailers.ui.detail.adapter.SimilarTvListAdapter
import com.an.trailers.ui.detail.adapter.VideoListAdapter
import com.an.trailers.ui.detail.viewmodel.TvDetailViewModel
import com.an.trailers.utils.AppUtils
import com.an.trailers.utils.NavigationUtils
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection

import javax.inject.Inject
import java.util.Arrays

class TvDetailActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: DetailActivityBinding
    private lateinit var tvDetailViewModel: TvDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        initialiseView()
        initialiseViewModel()
    }

    private fun initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val tvEntity = intent.getParcelableExtra<TvEntity>(INTENT_MOVIE)
        Picasso.get().load(tvEntity.getFormattedPosterPath()).into(binding.image)
        ViewCompat.setTransitionName(binding.image, TRANSITION_IMAGE_NAME)
        binding.expandButton.paintFlags = binding.expandButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun initialiseViewModel() {
        tvDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(TvDetailViewModel::class.java)
        tvDetailViewModel.fetchMovieDetail(intent.getParcelableExtra(INTENT_MOVIE))
        tvDetailViewModel.getTvDetailsLiveData().observe(this, Observer { tvEntity ->
            if (tvEntity != null) {
                updateMovieDetailView(tvEntity)
                if (tvEntity.videos != null && !tvEntity.videos!!.isEmpty()) {
                    updateMovieVideos(tvEntity.videos!!)
                }
                if (tvEntity.crews != null && !tvEntity.crews!!.isEmpty()) {
                    updateMovieCrewDetails(tvEntity.crews!!)
                }

                if (tvEntity.casts != null && !tvEntity.casts!!.isEmpty()) {
                    binding.expandButton.visibility = View.VISIBLE
                    updateMovieCastDetails(tvEntity.casts!!)
                }
                if (tvEntity.similarTvEntities != null && !tvEntity.similarTvEntities!!.isEmpty()) {
                    updateSimilarMoviesView(tvEntity.similarTvEntities!!)
                }
            }
        })
    }


    private fun updateMovieDetailView(tvEntity: TvEntity) {
        binding.movieTitle.text = tvEntity.header
        binding.movieDesc.text = tvEntity.description
        if (tvEntity.status != null) binding.movieStatus.items = Arrays.asList(tvEntity.status)
        binding.collectionItemPicker.isUseRandomColor = true
        if (tvEntity.genres != null) binding.collectionItemPicker.items = AppUtils.getGenres(tvEntity.genres!!)
        if (tvEntity.numberOfSeasons != null) binding.txtRuntime.text =
                AppUtils.getSeasonNumber(tvEntity.numberOfSeasons)
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

    private fun updateSimilarMoviesView(tvEntities: List<TvEntity>) {
        binding.includedSimilarLayout.moviesList.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.includedSimilarLayout.moviesList.visibility = View.VISIBLE
        val similarTvListAdapter = SimilarTvListAdapter(this, tvEntities)
        binding.includedSimilarLayout.moviesList.adapter = similarTvListAdapter
        binding.includedSimilarLayout.moviesList.addOnItemTouchListener(
                RecyclerItemClickListener(applicationContext,
                        object : RecyclerItemClickListener.OnRecyclerViewItemClickListener {
                            override fun onItemClick(parentView: View, childView: View, position: Int) {
                                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        this@TvDetailActivity, childView, TRANSITION_IMAGE_NAME)

                                NavigationUtils.redirectToTvDetailScreen(
                                        this@TvDetailActivity, similarTvListAdapter.getItem(position), options)
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
