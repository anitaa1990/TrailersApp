package com.an.trailers.data.repository


import com.an.trailers.data.NetworkBoundResource
import com.an.trailers.data.Resource
import com.an.trailers.data.local.converter.VideoListTypeConverter
import com.an.trailers.data.local.dao.MovieDao
import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.remote.api.MovieApiService
import com.an.trailers.data.remote.model.CreditResponse
import com.an.trailers.data.remote.model.MovieApiResponse
import com.an.trailers.data.remote.model.VideoResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Function4

import javax.inject.Singleton
import java.util.ArrayList

@Singleton
class MovieRepository(
    private val movieDao: MovieDao,
    private val movieApiService: MovieApiService
) {

    fun loadMoviesByType(type: String): Observable<Resource<List<MovieEntity>>> {
        return object : NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            override fun saveCallResult(item: MovieApiResponse) {
                val movieEntities = ArrayList<MovieEntity>()
                for (movieEntity in item.results) {
                    movieEntity.categoryType = type
                    movieEntities.add(movieEntity)
                }
                movieDao.insertMovies(movieEntities)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<MovieEntity>> {
                return movieDao.getMoviesByType(type)
            }

            override fun createCall(): Observable<Resource<MovieApiResponse>> {
                return movieApiService.fetchMoviesByType(type, 1)
                    .flatMap { movieApiResponse ->
                        Observable.just(
                            if (movieApiResponse == null)
                                Resource.error("", MovieApiResponse(1, emptyList(), 0, 1))
                            else
                                Resource.success(movieApiResponse)
                        )
                    }
            }
        }.getAsObservable()
    }


    fun fetchMovieDetails(movieId: Long?): Observable<Resource<MovieEntity>> {
        return object : NetworkBoundResource<MovieEntity, MovieEntity>() {
            override fun saveCallResult(item: MovieEntity) {
                val movieEntity: MovieEntity = movieDao.getMovieById(movieId)
                if(null == movieEntity) movieDao.insertMovie(item)
                else movieDao.updateMovie(item)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<MovieEntity> {
                val movieEntity: MovieEntity = movieDao.getMovieById(movieId)
                if(null == movieEntity) return Flowable.empty()
                return Flowable.just(movieEntity)
            }

            override fun createCall(): Observable<Resource<MovieEntity>> {
                val id = movieId.toString()
                return Observable.combineLatest(
                    movieApiService.fetchMovieDetail(id),
                    movieApiService.fetchMovieVideo(id),
                    movieApiService.fetchCastDetail(id),
                    movieApiService.fetchSimilarMovie(id, 1),
                    Function4
                    { movieEntity: MovieEntity,
                      videoResponse: VideoResponse,
                      creditResponse: CreditResponse,
                      movieApiResponse: MovieApiResponse ->

                        if (videoResponse != null) {
                            movieEntity.videos = videoResponse.results
                        }

                        if (creditResponse != null) {
                            movieEntity.crews = creditResponse.crew
                            movieEntity.casts = creditResponse.cast
                        }

                        if (movieApiResponse != null) {
                            movieEntity.similarMovies = movieApiResponse.results
                        }
                        Resource.success(movieEntity)
                    })
            }
        }.getAsObservable()
    }


    fun searchMovies(query: String): Observable<Resource<List<MovieEntity>>> {
        return object : NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            override fun saveCallResult(item: MovieApiResponse) {
                val movieEntities = ArrayList<MovieEntity>()
                for (movieEntity in item.results) {
                    movieEntity.categoryType = query
                    movieEntities.add(movieEntity)
                }
                movieDao.insertMovies(movieEntities)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<MovieEntity>> {
                return movieDao.getMoviesByType(query)
            }

            override fun createCall(): Observable<Resource<MovieApiResponse>> {
                return movieApiService.searchMoviesByQuery(query, "1")
                    .flatMap { movieApiResponse ->
                        Observable.just(
                            if (movieApiResponse == null) Resource.error("", MovieApiResponse(1, emptyList(), 0, 1))
                            else Resource.success(movieApiResponse)
                        )

                    }
            }
        }.getAsObservable()
    }

}
