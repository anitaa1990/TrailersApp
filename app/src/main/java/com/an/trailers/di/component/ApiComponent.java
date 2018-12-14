package com.an.trailers.di.component;


import com.an.trailers.di.module.ApiModule;
import com.an.trailers.di.module.AppModule;
import com.an.trailers.di.module.DbModule;
import com.an.trailers.ui.detail.viewmodel.MovieDetailViewModel;
import com.an.trailers.ui.detail.viewmodel.TvDetailViewModel;
import com.an.trailers.ui.main.viewmodel.MovieListViewModel;
import com.an.trailers.ui.main.viewmodel.TvListViewModel;
import com.an.trailers.ui.search.viewmodel.MovieSearchViewModel;
import com.an.trailers.ui.search.viewmodel.TvSearchViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class, DbModule.class})
public interface ApiComponent {

    void inject(MovieListViewModel moviesListViewModel);
    void inject(MovieDetailViewModel moviesDetailViewModel);
    void inject(MovieSearchViewModel movieSearchViewModel);


    void inject(TvListViewModel moviesListViewModel);
    void inject(TvDetailViewModel tvDetailViewModel);
    void inject(TvSearchViewModel tvSearchViewModel);
}
