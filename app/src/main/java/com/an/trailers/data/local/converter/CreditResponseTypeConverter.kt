package com.an.trailers.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.an.trailers.data.remote.model.CreditResponse;
import com.an.trailers.data.remote.model.Genre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CreditResponseTypeConverter {

    @TypeConverter
    public CreditResponse fromString(String value) {
        Type listType = new TypeToken<CreditResponse>() {}.getType();
        CreditResponse creditResponse = new Gson().fromJson(value, listType);
        return creditResponse;
    }

    @TypeConverter
    public String fromList(CreditResponse creditResponse) {
        return new Gson().toJson(creditResponse);
    }
}
