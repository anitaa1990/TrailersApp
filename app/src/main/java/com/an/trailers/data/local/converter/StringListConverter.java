package com.an.trailers.data.local.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StringListConverter {

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
}
