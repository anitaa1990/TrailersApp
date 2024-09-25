package com.an.trailers.ui.search.activity;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.an.trailers.R;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.databinding.SearchActivityBinding;
import com.an.trailers.factory.ViewModelFactory;
import com.an.trailers.ui.base.BaseActivity;
import com.an.trailers.ui.base.custom.recyclerview.PagerSnapHelper;
import com.an.trailers.ui.base.custom.recyclerview.RecyclerItemClickListener;
import com.an.trailers.ui.search.adapter.MovieSearchListAdapter;
import com.an.trailers.ui.search.viewmodel.MovieSearchViewModel;
import com.an.trailers.utils.AppUtils;
import com.an.trailers.utils.NavigationUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MovieSearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, RecyclerItemClickListener.OnRecyclerViewItemClickListener {

    @Inject
    ViewModelFactory viewModelFactory;

    MovieSearchViewModel searchViewModel;
    private SearchActivityBinding binding;
    private MovieSearchListAdapter searchListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initialiseView();
        initialiseViewModel();
    }

    private void initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        binding.search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        binding.search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        binding.search.setIconifiedByDefault(false);
        binding.search.setOnQueryTextListener(this);

        EditText searchEditText = binding.search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        Typeface myCustomFont = ResourcesCompat.getFont(getApplicationContext(), R.font.gt_medium);
        searchEditText.setTypeface(myCustomFont);

        searchListAdapter = new MovieSearchListAdapter(this);
        binding.includedLayout.moviesList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.includedLayout.moviesList.setAdapter(searchListAdapter);
        SnapHelper startSnapHelper = new PagerSnapHelper(position -> {
            MovieEntity trailer = searchListAdapter.getItem(position);
            updateBackground(trailer.getPosterPath());
        });
        startSnapHelper.attachToRecyclerView(binding.includedLayout.moviesList);
        binding.includedLayout.moviesList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), this));
    }

    private void initialiseViewModel() {
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieSearchViewModel.class);
    }

    private void updateBackground(String url) {
        binding.overlayLayout.updateCurrentBackground(url);
    }

    private void querySearch(String text) {
        searchViewModel.searchMovie(text);
        searchViewModel.getMoviesLiveData().observe(this, resource -> {
            if(resource.isLoading()) {
                displayLoader();

            } else if(resource.data != null && !resource.data.isEmpty()) {
                handleSuccessResponse(resource.data);

            } else handleErrorResponse();
        });
    }

    private void handleSuccessResponse(List<MovieEntity> movies) {
        hideLoader();
        binding.includedLayout.emptyLayout.emptyContainer.setVisibility(View.GONE);
        binding.includedLayout.moviesList.setVisibility(View.VISIBLE);
        searchListAdapter.setItems(movies);
        new Handler().postDelayed(() -> {
            if(searchListAdapter.getItemCount() > 0) {
                updateBackground(searchListAdapter.getItem(0).getPosterPath());
            }

        }, 400);
    }

    private void handleErrorResponse() {
        hideLoader();
        binding.includedLayout.moviesList.setVisibility(View.GONE);
        binding.includedLayout.emptyLayout.emptyContainer.setVisibility(View.VISIBLE);
    }


    private void displayLoader() {
        binding.includedLayout.moviesList.setVisibility(View.GONE);
        binding.includedLayout.loaderLayout.rootView.setVisibility(View.VISIBLE);
        binding.includedLayout.loaderLayout.loader.start();
    }

    private void hideLoader() {
        binding.includedLayout.moviesList.setVisibility(View.VISIBLE);
        binding.includedLayout.loaderLayout.rootView.setVisibility(View.GONE);
        binding.includedLayout.loaderLayout.loader.stop();
    }

    @Override
    public void onItemClick(View parentView, View childView, int position) {
        searchViewModel.getMoviesLiveData().removeObservers(this);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                new Pair(childView.findViewById(R.id.image), TRANSITION_IMAGE_NAME));
        NavigationUtils.redirectToDetailScreen(this,
                searchListAdapter.getItem(position),
                options);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        AppUtils.closeKeyboard(this);
        querySearch(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
