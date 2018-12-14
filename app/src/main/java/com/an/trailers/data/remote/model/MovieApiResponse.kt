package com.an.trailers.data.remote.model


import com.an.trailers.data.local.entity.MovieEntity

data class MovieApiResponse(val page: Int,
                            val results: List<MovieEntity>,
                            val total_results: Int,
                            val total_pages: Int)
