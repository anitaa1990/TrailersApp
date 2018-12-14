package com.an.trailers.data.remote.model


import com.an.trailers.data.local.entity.TvEntity

data class TvApiResponse(val page: Int,
                         val results: List<TvEntity>,
                         val total_results: Int,
                         val total_pages: Int)
