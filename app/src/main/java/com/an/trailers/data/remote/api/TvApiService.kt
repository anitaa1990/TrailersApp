package com.an.trailers.data.remote.api


import com.an.trailers.data.local.entity.TvEntity
import com.an.trailers.data.remote.model.CreditResponse
import com.an.trailers.data.remote.model.TvApiResponse
import com.an.trailers.data.remote.model.VideoResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvApiService {

    @GET("tv/{type}?language=en-US&region=US")
    fun fetchTvListByType(
        @Path("type") type: String,
        @Query("page") page: Long
    ): Observable<TvApiResponse>


    @GET("/3/tv/{tvId}")
    fun fetchTvDetail(@Path("tvId") tvId: String): Observable<TvEntity>


    @GET("/3/tv/{tvId}/videos")
    fun fetchTvVideo(@Path("tvId") tvId: String): Observable<VideoResponse>

    @GET("/3/tv/{tvId}/credits")
    fun fetchCastDetail(@Path("tvId") tvId: String): Observable<CreditResponse>


    @GET("/3/tv/{tvId}/similar")
    fun fetchSimilarTvList(
        @Path("tvId") tvId: String,
        @Query("page") page: Long
    ): Observable<TvApiResponse>


    @GET("/3/search/tv")
    fun searchTvsByQuery(
        @Query("query") query: String,
        @Query("page") page: String
    ): Observable<TvApiResponse>
}
