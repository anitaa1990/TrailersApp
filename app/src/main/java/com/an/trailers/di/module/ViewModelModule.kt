package com.an.trailers.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.an.trailers.factory.ViewModelFactory
import com.an.trailers.ui.detail.viewmodel.MovieDetailViewModel
import com.an.trailers.ui.detail.viewmodel.TvDetailViewModel
import com.an.trailers.ui.main.viewmodel.MovieListViewModel
import com.an.trailers.ui.main.viewmodel.TvListViewModel
import com.an.trailers.ui.search.viewmodel.MovieSearchViewModel
import com.an.trailers.ui.search.viewmodel.TvSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel::class)
    protected abstract fun movieListViewModel(moviesListViewModel: MovieListViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    protected abstract fun movieDetailViewModel(moviesDetailViewModel: MovieDetailViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MovieSearchViewModel::class)
    protected abstract fun movieSearchViewModel(movieSearchViewModel: MovieSearchViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TvListViewModel::class)
    protected abstract fun tvListViewModel(tvListViewModel: TvListViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TvDetailViewModel::class)
    protected abstract fun tvDetailViewModel(tvDetailViewModel: TvDetailViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TvSearchViewModel::class)
    protected abstract fun tvSearchViewModel(tvSearchViewModel: TvSearchViewModel): ViewModel
}