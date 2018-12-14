package com.an.trailers.data.remote.api


import com.an.trailers.data.local.entity.MovieEntity
import com.an.trailers.data.remote.model.CreditResponse
import com.an.trailers.data.remote.model.MovieApiResponse
import com.an.trailers.data.remote.model.VideoResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/{type}?language=en-US&region=US")
    fun fetchMoviesByType(
        @Path("type") type: String,
        @Query("page") page: Long
    ): Observable<MovieApiResponse>


    @GET("/3/movie/{movieId}")
    fun fetchMovieDetail(@Path("movieId") movieId: String): Observable<MovieEntity>


    @GET("/3/movie/{movieId}/videos")
    fun fetchMovieVideo(@Path("movieId") movieId: String): Observable<VideoResponse>

    @GET("/3/movie/{movieId}/credits")
    fun fetchCastDetail(@Path("movieId") movieId: String): Observable<CreditResponse>


    @GET("/3/movie/{movieId}/similar")
    fun fetchSimilarMovie(
        @Path("movieId") movieId: String,
        @Query("page") page: Long
    ): Observable<MovieApiResponse>


    @GET("/3/search/movie")
    fun searchMoviesByQuery(
        @Query("query") query: String,
        @Query("page") page: String
    ): Observable<MovieApiResponse>
}
