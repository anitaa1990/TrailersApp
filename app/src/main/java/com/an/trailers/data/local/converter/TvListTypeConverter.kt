package com.an.trailers.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.an.trailers.data.local.entity.TvEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TvListTypeConverter {

    @TypeConverter
    public List<TvEntity> fromString(String value) {
        Type listType = new TypeToken<List<TvEntity>>() {}.getType();
        List<TvEntity> tvEntities = new Gson().fromJson(value, listType);
        return tvEntities;
    }

    @TypeConverter
    public String fromList(List<TvEntity> movieEntities) {
        return new Gson().toJson(movieEntities);
    }
}
