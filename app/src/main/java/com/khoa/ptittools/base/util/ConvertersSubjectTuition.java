package com.khoa.ptittools.base.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khoa.ptittools.base.model.SubjectTuition;

import java.lang.reflect.Type;
import java.util.List;

public class ConvertersSubjectTuition {
    @TypeConverter
    public static List<SubjectTuition> fromString(String value) {
        Type listType = new TypeToken<List<SubjectTuition>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<SubjectTuition> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}