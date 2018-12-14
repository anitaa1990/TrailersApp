package com.an.trailers.di.module

import com.an.trailers.ui.detail.activity.MovieDetailActivity
import com.an.trailers.ui.detail.activity.TvDetailActivity
import com.an.trailers.ui.main.activity.MainActivity
import com.an.trailers.ui.search.activity.MovieSearchActivity
import com.an.trailers.ui.search.activity.TvSearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailActivity(): MovieDetailActivity

    @ContributesAndroidInjector
    abstract fun contributeTvDetailActivity(): TvDetailActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieSearchActivity(): MovieSearchActivity

    @ContributesAndroidInjector
    abstract fun contributeTvSearchActivity(): TvSearchActivity
}