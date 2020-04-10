package com.khoa.ptittools.base.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ConvertersUtil {
    @TypeConverter
    public static <T> List<T> fromString(String value) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static <T> String fromList(List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static <T> String fromObject(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }
}
