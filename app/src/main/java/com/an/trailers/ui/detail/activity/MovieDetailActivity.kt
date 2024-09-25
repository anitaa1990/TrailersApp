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
import com.an.trailers.data.local.entity.MovieEntity;
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
import com.an.trailers.ui.detail.adapter.SimilarMoviesListAdapter;
import com.an.trailers.ui.detail.adapter.VideoListAdapter;
import com.an.trailers.ui.detail.viewmodel.MovieDetailViewModel;
import com.an.trailers.utils.AppUtils;
import com.an.trailers.utils.NavigationUtils;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MovieDetailActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private DetailActivityBinding binding;
    MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initialiseView();
        initialiseViewModel();
    }

    private void initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        MovieEntity movie = getIntent().getParcelableExtra(INTENT_MOVIE);
        Picasso.get().load(movie.getPosterPath()).into(binding.image);
        ViewCompat.setTransitionName(binding.image, TRANSITION_IMAGE_NAME);
        binding.expandButton.setPaintFlags(binding.expandButton.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);
    }


    private void initialiseViewModel() {
        movieDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel.class);
        movieDetailViewModel.fetchMovieDetail(getIntent().getParcelableExtra(INTENT_MOVIE));
        movieDetailViewModel.getMovieDetailsLiveData().observe(this, movieEntity -> {
            updateMovieDetailView(movieEntity);
            if(movieEntity.getVideos() != null && !movieEntity.getVideos().isEmpty()) {
                updateMovieVideos(movieEntity.getVideos());
            }
            if(movieEntity.getCrews() != null && !movieEntity.getCrews().isEmpty()) {
                updateMovieCrewDetails(movieEntity.getCrews());
            }

            if(movieEntity.getCasts() != null && !movieEntity.getCasts().isEmpty()) {
                binding.expandButton.setVisibility(View.VISIBLE);
                updateMovieCastDetails(movieEntity.getCasts());
            }
            if(movieEntity.getSimilarMovies() != null && !movieEntity.getSimilarMovies().isEmpty()) {
                updateSimilarMoviesView(movieEntity.getSimilarMovies());
            }
            if(movieEntity.getReviews() != null && !movieEntity.getReviews().isEmpty()) {
                updateMovieReviews(movieEntity.getReviews());
            } else binding.includedReviewsLayout.reviewView.setVisibility(View.GONE);
        });
    }


    private void updateMovieDetailView(MovieEntity movie) {
        binding.movieTitle.setText(movie.getHeader());
        binding.movieDesc.setText(movie.getDescription());
        if(movie.getStatus() != null) binding.movieStatus.setItems(Arrays.asList(new String[]{ movie.getStatus() }));
        binding.collectionItemPicker.setUseRandomColor(true);
        if(movie.getGenres() != null) binding.collectionItemPicker.setItems(AppUtils.getGenres(movie.getGenres()));
        binding.txtRuntime.setText(AppUtils.getRunTimeInMins(movie.getStatus(), movie.getRuntime(), movie.getReleaseDate()));
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


    private void updateMovieReviews(List<Review> reviews) {
        binding.includedReviewsLayout.reviewsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.includedReviewsLayout.reviewsList.setVisibility(View.VISIBLE);
        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(reviews);
        binding.includedReviewsLayout.reviewsList.setAdapter(reviewListAdapter);
        binding.includedReviewsLayout.reviewView.setVisibility(View.VISIBLE);
    }

    private void updateSimilarMoviesView(List<MovieEntity> movies) {
        binding.includedSimilarLayout.moviesList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.includedSimilarLayout.moviesList.setVisibility(View.VISIBLE);
        SimilarMoviesListAdapter similarMoviesListAdapter = new SimilarMoviesListAdapter(this, movies);
        binding.includedSimilarLayout.moviesList.setAdapter(similarMoviesListAdapter);

        binding.includedSimilarLayout.moviesList.addOnItemTouchListener(new RecyclerItemClickListener(this, (parentView, childView, position) -> {
            MovieEntity movie = similarMoviesListAdapter.getItem(position);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    new Pair(childView, TRANSITION_IMAGE_NAME));
            NavigationUtils.redirectToDetailScreen(this, movie, options);
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
