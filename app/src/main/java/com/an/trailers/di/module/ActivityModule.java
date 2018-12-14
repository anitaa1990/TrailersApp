package com.an.trailers.di.module;

import com.an.trailers.ui.detail.activity.MovieDetailActivity;
import com.an.trailers.ui.detail.activity.TvDetailActivity;
import com.an.trailers.ui.main.activity.MainActivity;
import com.an.trailers.ui.search.activity.MovieSearchActivity;
import com.an.trailers.ui.search.activity.TvSearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract MovieDetailActivity contributeMovieDetailActivity();

    @ContributesAndroidInjector()
    abstract TvDetailActivity contributeTvDetailActivity();

    @ContributesAndroidInjector()
    abstract MovieSearchActivity contributeMovieSearchActivity();

    @ContributesAndroidInjector()
    abstract TvSearchActivity contributeTvSearchActivity();
}