package com.an.trailers.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.an.trailers.data.remote.model.Cast;
import com.an.trailers.data.remote.model.CreditResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CastListTypeConverter {

    @TypeConverter
    public List<Cast> fromString(String value) {
        Type listType = new TypeToken<List<Cast>>() {}.getType();
        List<Cast> casts = new Gson().fromJson(value, listType);
        return casts;
    }

    @TypeConverter
    public String fromList(List<Cast> casts) {
        return new Gson().toJson(casts);
    }
}
