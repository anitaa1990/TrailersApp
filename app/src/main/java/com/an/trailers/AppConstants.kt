package com.an.trailers

import java.util.Collections
import java.util.HashMap

interface AppConstants {
    companion object {
        const val PAGE_LIMIT = 10

        const val CREDIT_CAST = "cast"
        const val CREDIT_CREW = "crew"

        const val INTENT_MOVIE = "movie"
        const val INTENT_CATEGORY = "category"
        const val INTENT_VIDEO_KEY = "intent_video_key"

        const val TRANSITION_IMAGE_NAME = "image"

        const val TYPE_MOVIES = "movie"
        const val TYPE_TVS = "tv"
        const val MOVIES_POPULAR = "popular"
        const val MOVIES_UPCOMING = "upcoming"
        const val MOVIES_TOP_RATED = "top_rated"
        const val TV_ON_THE_AIR = "on_the_air"

        const val MOVIE_STATUS_RELEASED = "Released"

        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500%s"

        const val TMDB_API_KEY = "5e74ee79280d770dc8ed5a2fbdda955a"
        const val YOUTUBE_API_KEY = "AIzaSyCZY8Vnw_6GcJcESL-NilTZDMSvg9ViLt8"

        val MENU_MOVIE_ITEM: MutableMap<Int, String> = Collections.unmodifiableMap(
            object : HashMap<Int, String>() {
                init {
                    put(0, MOVIES_POPULAR)
                    put(1, MOVIES_UPCOMING)
                    put(2, MOVIES_TOP_RATED)
                }
            })

        val MENU_TV_ITEM: MutableMap<Int, String> = Collections.unmodifiableMap(
            object : HashMap<Int, String>() {
                init {
                    put(0, MOVIES_POPULAR)
                    put(1, TV_ON_THE_AIR)
                    put(2, MOVIES_TOP_RATED)
                }
            })


        val MENU_ITEM: MutableMap<Int, String> = Collections.unmodifiableMap(
            object : HashMap<Int, String>() {
                init {
                    put(R.id.btn_movie, TYPE_MOVIES)
                    put(R.id.btn_tv, TYPE_TVS)
                }
            })
    }
}
