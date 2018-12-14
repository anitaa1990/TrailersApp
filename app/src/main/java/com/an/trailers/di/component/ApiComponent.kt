package com.an.trailers.di.component


import android.app.Application
import com.an.trailers.AppController
import com.an.trailers.di.module.*
import com.an.trailers.ui.detail.viewmodel.MovieDetailViewModel
import com.an.trailers.ui.detail.viewmodel.TvDetailViewModel
import com.an.trailers.ui.main.viewmodel.MovieListViewModel
import com.an.trailers.ui.main.viewmodel.TvListViewModel
import com.an.trailers.ui.search.viewmodel.MovieSearchViewModel
import com.an.trailers.ui.search.viewmodel.TvSearchViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApiModule::class, DbModule::class,
        ViewModelModule::class, AndroidSupportInjectionModule::class,
        ActivityModule::class, FragmentModule::class]
)
interface ApiComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun apiModule(apiModule: ApiModule): Builder

        @BindsInstance
        fun dbModule(dbModule: DbModule): Builder

        fun build(): ApiComponent
    }

    fun inject(appController: AppController)
}
