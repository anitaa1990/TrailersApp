package com.an.trailers.di.module

import com.an.trailers.ui.main.fragment.MovieListFragment
import com.an.trailers.ui.main.fragment.TvListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment

    @ContributesAndroidInjector
    abstract fun contributeTvListFragment(): TvListFragment
}