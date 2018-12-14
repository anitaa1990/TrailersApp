package com.an.trailers

import java.util.Collections
import java.util.HashMap

interface AppConstants {
    companion object {

        val PAGE_LIMIT = 10


        val CREDIT_CAST = "cast"
        val CREDIT_CREW = "crew"

        val INTENT_MOVIE = "movie"
        val INTENT_CATEGORY = "category"
        val INTENT_VIDEO_KEY = "intent_video_key"

        val TRANSITION_IMAGE_NAME = "image"

        val TYPE_MOVIES = "movie"
        val TYPE_TVS = "tv"
        val MOVIES_POPULAR = "popular"
        val MOVIES_UPCOMING = "upcoming"
        val MOVIES_TOP_RATED = "top_rated"
        val TV_ON_THE_AIR = "on_the_air"

        val MOVIE_STATUS_RELEASED = "Released"

        val BASE_URL = "https://api.themoviedb.org/3/"
        val IMAGE_URL = "https://image.tmdb.org/t/p/w500%s"

        val TMDB_API_KEY = "5e74ee79280d770dc8ed5a2fbdda955a"
        val YOUTUBE_API_KEY = "AIzaSyCZY8Vnw_6GcJcESL-NilTZDMSvg9ViLt8"

        val MENU_MOVIE_ITEM = Collections.unmodifiableMap(
            object : HashMap<Int, String>() {
                init {
                    put(0, MOVIES_POPULAR)
                    put(1, MOVIES_UPCOMING)
                    put(2, MOVIES_TOP_RATED)
                }
            })

        val MENU_TV_ITEM = Collections.unmodifiableMap(
            object : HashMap<Int, String>() {
                init {
                    put(0, MOVIES_POPULAR)
                    put(1, TV_ON_THE_AIR)
                    put(2, MOVIES_TOP_RATED)
                }
            })


        val MENU_ITEM = Collections.unmodifiableMap(
            object : HashMap<Int, String>() {
                init {
                    put(R.id.btn_movie, TYPE_MOVIES)
                    put(R.id.btn_tv, TYPE_TVS)
                }
            })
    }
}