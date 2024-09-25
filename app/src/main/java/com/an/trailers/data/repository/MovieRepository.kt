package com.an.trailers.data.repository


import com.an.trailers.data.NetworkBoundResource
import com.an.trailers.data.Resource
import com.an.trailers.data.local.dao.MovieDao
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.remote.api.MovieApiService
import com.an.trailers.data.remote.model.CreditResponse
import com.an.trailers.data.remote.model.MovieApiResponse
import com.an.trailers.data.remote.model.VideoResponse
import com.an.trailers.utils.AppUtils
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
class MovieRepository(
    private val movieDao: MovieDao,
    private val movieApiService: MovieApiService
) {
   fun loadMoviesByType(page: Long,
                        type: String
   ): Observable<Resource<List<MovieEntity>>> {
        return object : NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            override fun saveCallResult(item: MovieApiResponse) {
                val movieEntities = ArrayList<MovieEntity>()
                for (movieEntity in item.results) {
                    val storedEntity = movieDao.getMovieById(movieEntity.id)
                    if(storedEntity == null) {
                        movieEntity.categoryTypes = listOf(type)
                    } else {
                        val categories: MutableList<String> = mutableListOf()
                        if(storedEntity.categoryTypes != null) categories.addAll(storedEntity.categoryTypes!!)
                        categories.add(type)
                        movieEntity.categoryTypes = categories
                    }

                    movieEntity.page = item.page
                    movieEntity.totalPages = item.totalPages
                    movieEntities.add(movieEntity)
                }
                movieDao.insertMovies(movieEntities)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<MovieEntity>> {
                val movieEntities = movieDao.getMoviesByPage(page)
                return if (movieEntities.isEmpty()) {
                    Flowable.empty()
                } else Flowable.just(AppUtils.getMoviesByType(type, movieEntities))
            }

            override fun createCall(): Observable<Resource<MovieApiResponse>> {
                return movieApiService.fetchMoviesByType(type, page)
                    .flatMap { movieApiResponse ->
                        Observable.just(
                            if (movieApiResponse.results.isEmpty())
                                Resource.error("", MovieApiResponse(1, emptyList(), 0, 1))
                            else Resource.success(movieApiResponse)
                        )
                    }
            }
        }.getAsObservable()
    }


    fun fetchMovieDetails(movieId: Long): Observable<Resource<MovieEntity>> {
        return object : NetworkBoundResource<MovieEntity, MovieEntity>() {
            override fun saveCallResult(item: MovieEntity) {
                val movieEntity = movieDao.getMovieById(movieId)
                if(null == movieEntity) movieDao.insertMovie(item)
                else {
                    item.page = movieEntity.page
                    item.totalPages = movieEntity.totalPages
                    item.categoryTypes = movieEntity.categoryTypes
                    movieDao.updateMovie(item)
                }
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<MovieEntity> {
                val movieEntity = movieDao.getMovieById(movieId) ?: return Flowable.empty()
                return Flowable.just(movieEntity)
            }

            override fun createCall(): Observable<Resource<MovieEntity>> {
                val id = movieId.toString()
                return Observable.combineLatest(
                    movieApiService.fetchMovieDetail(id),
                    movieApiService.fetchMovieVideo(id),
                    movieApiService.fetchCastDetail(id),
                    movieApiService.fetchSimilarMovie(id, 1)
                ) { movieEntity: MovieEntity,
                    videoResponse: VideoResponse,
                    creditResponse: CreditResponse,
                    movieApiResponse: MovieApiResponse ->

                    if (videoResponse.results.isNotEmpty()) {
                        movieEntity.videos = videoResponse.results
                    }
                    if (creditResponse.crew.isNotEmpty()) {
                        movieEntity.crews = creditResponse.crew
                    }
                    if (creditResponse.cast.isNotEmpty()) {
                        movieEntity.casts = creditResponse.cast
                    }
                    if (movieApiResponse.results.isNotEmpty()) {
                        movieEntity.similarMovies = movieApiResponse.results
                    }
                    Resource.success(movieEntity)
                }
            }
        }.getAsObservable()
    }


    fun searchMovies(
        page: Long,
        query: String
    ): Observable<Resource<List<MovieEntity>>> {
        return object : NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            override fun saveCallResult(item: MovieApiResponse) {
                val movieEntities = ArrayList<MovieEntity>()
                for (movieEntity in item.results) {
                    val storedEntity = movieDao.getMovieById(movieEntity.id)
                    if(storedEntity == null) {
                        movieEntity.categoryTypes = listOf(query)
                    } else {
                        val categories: MutableList<String> = mutableListOf()
                        if(!storedEntity.categoryTypes.isNullOrEmpty()) categories.addAll(storedEntity.categoryTypes!!)
                        categories.add(query)
                        movieEntity.categoryTypes = categories
                    }

                    movieEntity.page = item.page
                    movieEntity.totalPages = item.totalPages
                    movieEntities.add(movieEntity)
                }
                movieDao.insertMovies(movieEntities)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<MovieEntity>> {
                val movieEntities = movieDao.getMoviesByPage(page)
                return if (movieEntities.isEmpty()) {
                    Flowable.empty()
                } else Flowable.just(AppUtils.getMoviesByType(query, movieEntities))
            }

            override fun createCall(): Observable<Resource<MovieApiResponse>> {
                return movieApiService.searchMoviesByQuery(query, "1")
                    .flatMap { movieApiResponse ->
                        Observable.just(
                            if (movieApiResponse.results.isEmpty()) {
                                Resource.error("", MovieApiResponse(1, emptyList(), 0, 1))
                            } else Resource.success(movieApiResponse)
                        )

                    }
            }
        }.getAsObservable()
    }

}
