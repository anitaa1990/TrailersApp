package com.an.trailers.ui.main.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.an.trailers.R;
import com.an.trailers.data.local.entity.TvEntity;
import com.an.trailers.databinding.MoviesListFragmentBinding;
import com.an.trailers.factory.ViewModelFactory;
import com.an.trailers.ui.base.BaseFragment;
import com.an.trailers.ui.base.custom.recyclerview.PagerSnapHelper;
import com.an.trailers.ui.base.custom.recyclerview.RecyclerItemClickListener;
import com.an.trailers.ui.base.custom.recyclerview.RecyclerViewPaginator;
import com.an.trailers.ui.main.activity.MainActivity;
import com.an.trailers.ui.main.adapter.TvListAdapter;
import com.an.trailers.ui.main.viewmodel.TvListViewModel;
import com.an.trailers.utils.NavigationUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class TvListFragment extends BaseFragment implements RecyclerItemClickListener.OnRecyclerViewItemClickListener {

    @Inject
    ViewModelFactory viewModelFactory;

    TvListViewModel tvListViewModel;
    private TvListAdapter tvListAdapter;
    private MoviesListFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        initialiseViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseView();
    }

    private void initialiseView() {
        tvListAdapter = new TvListAdapter(activity);
        binding.moviesList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.moviesList.setAdapter(tvListAdapter);
        binding.moviesList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));

        SnapHelper startSnapHelper = new PagerSnapHelper(position -> {
            TvEntity movie = tvListAdapter.getItem(position);
            ((MainActivity)activity).updateBackground(movie.getPosterPath());
        });
        startSnapHelper.attachToRecyclerView(binding.moviesList);

        binding.moviesList.addOnScrollListener(new RecyclerViewPaginator(binding.moviesList) {
            @Override
            public boolean isLastPage() {
                return tvListViewModel.isLastPage();
            }

            @Override
            public void loadMore(Long page) {
                tvListViewModel.loadMoreTvs(page);
            }

            @Override
            public void loadFirstData(Long page) {
                displayLoader();
                tvListViewModel.loadMoreTvs(page);
            }
        });
    }


    private void initialiseViewModel() {
        tvListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TvListViewModel.class);
        tvListViewModel.setType(MENU_TV_ITEM.get(getArguments() == null ? 0: getArguments().getInt(INTENT_CATEGORY)));
        tvListViewModel.getTvsLiveData().observe(this, resource -> {
            if(resource.isLoading()) {

            } else if(!resource.data.isEmpty()) {
                updateTvsList(resource.data);

            } else handleErrorResponse();
        });
    }

    private void updateTvsList(List<TvEntity> movies) {
        hideLoader();
        binding.emptyLayout.emptyContainer.setVisibility(View.GONE);
        binding.moviesList.setVisibility(View.VISIBLE);
        tvListAdapter.setItems(movies);
//        new Handler().postDelayed(() -> {
//            if(tvListAdapter.getItemCount() > 0) {
//                ((MainActivity) activity).updateBackground(tvListAdapter.getItem(0).getPosterPath());
//            }
//
//        }, 400);
    }

    private void handleErrorResponse() {
        hideLoader();
        binding.moviesList.setVisibility(View.GONE);
        binding.emptyLayout.emptyContainer.setVisibility(View.VISIBLE);
        ((MainActivity) activity).clearBackground();
    }


    private void displayLoader() {
        binding.moviesList.setVisibility(View.GONE);
        binding.loaderLayout.rootView.setVisibility(View.VISIBLE);
        binding.loaderLayout.loader.start();
        ((MainActivity)activity).hideToolbar();
    }

    private void hideLoader() {
        binding.moviesList.setVisibility(View.VISIBLE);
        binding.loaderLayout.rootView.setVisibility(View.GONE);
        binding.loaderLayout.loader.stop();
        ((MainActivity)activity).displayToolbar();
    }

    @Override
    public void onItemClick(View parentView, View childView, int position) {
        tvListViewModel.onStop();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair(childView.findViewById(R.id.image), TRANSITION_IMAGE_NAME));
        NavigationUtils.redirectToTvDetailScreen(requireActivity(),
                tvListAdapter.getItem(position),
                options);
    }
}
