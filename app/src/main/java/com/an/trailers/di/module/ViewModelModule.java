package com.an.trailers.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.an.trailers.factory.ViewModelFactory;
import com.an.trailers.ui.detail.viewmodel.MovieDetailViewModel;
import com.an.trailers.ui.main.viewmodel.MovieListViewModel;
import com.an.trailers.ui.search.viewmodel.MovieSearchViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel.class)
    protected abstract ViewModel moviesListViewModel(MovieListViewModel moviesListViewModel);
    protected abstract ViewModel movieDetailViewModel(MovieDetailViewModel moviesDetailViewModel);
    protected abstract ViewModel searchViewModel(MovieSearchViewModel movieSearchViewModel);
}