package com.an.trailers.ui.detail.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.an.trailers.R;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.data.remote.model.Cast;
import com.an.trailers.data.remote.model.Crew;
import com.an.trailers.data.remote.model.Review;
import com.an.trailers.data.remote.model.Video;
import com.an.trailers.databinding.DetailActivityBinding;
import com.an.trailers.factory.ViewModelFactory;
import com.an.trailers.ui.base.BaseActivity;
import com.an.trailers.ui.base.custom.recyclerview.RecyclerItemClickListener;
import com.an.trailers.ui.detail.adapter.CreditListAdapter;
import com.an.trailers.ui.detail.adapter.ReviewListAdapter;
import com.an.trailers.ui.detail.adapter.SimilarTvListAdapter;
import com.an.trailers.ui.detail.adapter.VideoListAdapter;
import com.an.trailers.ui.detail.viewmodel.TvDetailViewModel;
import com.an.trailers.utils.AppUtils;
import com.an.trailers.utils.NavigationUtils;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class TvDetailActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private DetailActivityBinding binding;
    TvDetailViewModel tvDetailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initialiseView();
        initialiseViewModel();
    }

    private void initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        TvEntity tvEntity = getIntent().getParcelableExtra(INTENT_MOVIE);
        Picasso.get().load(tvEntity.getPosterPath()).into(binding.image);
        ViewCompat.setTransitionName(binding.image, TRANSITION_IMAGE_NAME);
        binding.expandButton.setPaintFlags(binding.expandButton.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);


    }

    private void initialiseViewModel() {
        tvDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(TvDetailViewModel.class);
        tvDetailViewModel.fetchMovieDetail(getIntent().getParcelableExtra(INTENT_MOVIE));
        tvDetailViewModel.getTvDetailsLiveData().observe(this, tvEntity -> {
            if(tvEntity != null) {
                updateMovieDetailView(tvEntity);
                if(tvEntity.getVideos() != null && !tvEntity.getVideos().isEmpty()) {
                    updateMovieVideos(tvEntity.getVideos());
                }
                if(tvEntity.getCrews() != null && !tvEntity.getCrews().isEmpty()) {
                    updateMovieCrewDetails(tvEntity.getCrews());
                }

                if(tvEntity.getCasts() != null && !tvEntity.getCasts().isEmpty()) {
                    binding.expandButton.setVisibility(View.VISIBLE);
                    updateMovieCastDetails(tvEntity.getCasts());
                }
                if(tvEntity.getSimilarTvEntities() != null && !tvEntity.getSimilarTvEntities().isEmpty()) {
                    updateSimilarMoviesView(tvEntity.getSimilarTvEntities());
                }
                if(tvEntity.getReviews() != null && !tvEntity.getReviews().isEmpty()) {
                    updateTvReviews(tvEntity.getReviews());
                } else binding.includedReviewsLayout.reviewView.setVisibility(View.GONE);
            }
        });
    }


    private void updateMovieDetailView(TvEntity tvEntity) {
        binding.movieTitle.setText(tvEntity.getHeader());
        binding.movieDesc.setText(tvEntity.getDescription());
        if(tvEntity.getStatus() != null) binding.movieStatus.setItems(Arrays.asList(new String[]{ tvEntity.getStatus() }));
        binding.collectionItemPicker.setUseRandomColor(true);
        if(tvEntity.getGenres() != null) binding.collectionItemPicker.setItems(AppUtils.getGenres(tvEntity.getGenres()));
        if(tvEntity.getNumberOfSeasons() != null) binding.txtRuntime.setText(AppUtils.getSeasonNumber(tvEntity.getNumberOfSeasons()));
    }

    private void updateMovieVideos(List<Video> videos) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.smoothScrollToPosition(1);

        VideoListAdapter videoListAdapter = new VideoListAdapter(getApplicationContext(), videos);
        binding.recyclerView.setAdapter(videoListAdapter);
        binding.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, (parentView, childView, position) -> {
            NavigationUtils.redirectToVideoScreen(this, videoListAdapter.getItem(position).getKey());
        }));
    }

    private void updateMovieCastDetails(List<Cast> casts) {
        binding.includedLayout.castList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.includedLayout.castList.setVisibility(View.VISIBLE);
        CreditListAdapter creditListAdapter = new CreditListAdapter(getApplicationContext(), casts);
        binding.includedLayout.castList.setAdapter(creditListAdapter);
    }

    private void updateMovieCrewDetails(List<Crew> crews) {
        binding.includedLayout.crewList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.includedLayout.castList.setVisibility(View.VISIBLE);
        CreditListAdapter creditListAdapter = new CreditListAdapter(getApplicationContext(), CREDIT_CREW, crews);
        binding.includedLayout.crewList.setAdapter(creditListAdapter);
    }

    private void updateTvReviews(List<Review> reviews) {
        binding.includedReviewsLayout.reviewsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.includedReviewsLayout.reviewsList.setVisibility(View.VISIBLE);
        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(reviews);
        binding.includedReviewsLayout.reviewsList.setAdapter(reviewListAdapter);
        binding.includedReviewsLayout.reviewView.setVisibility(View.VISIBLE);
    }

    private void updateSimilarMoviesView(List<TvEntity> tvEntities) {
        binding.includedSimilarLayout.moviesList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.includedSimilarLayout.moviesList.setVisibility(View.VISIBLE);
        SimilarTvListAdapter similarTvListAdapter = new SimilarTvListAdapter(this, tvEntities);
        binding.includedSimilarLayout.moviesList.setAdapter(similarTvListAdapter);

        binding.includedSimilarLayout.moviesList.addOnItemTouchListener(new RecyclerItemClickListener(this, (parentView, childView, position) -> {
            TvEntity tvEntity = similarTvListAdapter.getItem(position);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    new Pair(childView, TRANSITION_IMAGE_NAME));
            NavigationUtils.redirectToTvDetailScreen(this, tvEntity, options);
        }));
        binding.includedSimilarLayout.movieSimilarTitle.setVisibility(View.VISIBLE);
    }


    public void handleExpandAction(View view) {
        if (binding.includedLayout.expandableLayout.isExpanded()) {
            binding.expandButton.setText(getString(R.string.read_more));
            binding.includedLayout.expandableLayout.collapse();
        } else {
            binding.expandButton.setText(getString(R.string.read_less));
            binding.includedLayout.expandableLayout.expand();
        }
    }
}
