package com.an.trailers.data.remote.model


import com.an.trailers.data.local.entity.TvEntity

data class TvApiResponse(
    val page: Long,
    val results: List<TvEntity>,
    val totalResults: Long,
    val totalPages: Long
)
