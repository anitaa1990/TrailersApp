package com.an.trailers.data.local.converter

import androidx.room.TypeConverter
import com.an.trailers.data.remote.model.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VideoListTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<Video>? {
        val listType = object : TypeToken<List<Video>>() {}.type
        return Gson().fromJson<List<Video>>(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Video>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    fun fromVideos(videos: List<Video>?): List<String> {
        if (videos == null) return emptyList()
        val videosList = ArrayList<String>()
        for (video in videos) {
            videosList.add(video.key)
        }
        return videosList
    }
}
