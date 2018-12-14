package com.an.trailers.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.an.trailers.data.remote.model.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoListTypeConverter {

    @TypeConverter
    public List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> videos = new Gson().fromJson(value, listType);
        return videos;
    }

    @TypeConverter
    public String fromList(List<String> videos) {
        return new Gson().toJson(videos);
    }

    public List<String> fromVideos(List<Video> videos) {
        if(videos == null) return Collections.EMPTY_LIST;
        List<String> videosList = new ArrayList<>();
        for(Video video : videos) {
            videosList.add(video.getKey());
        }
        return videosList;
    }
}
