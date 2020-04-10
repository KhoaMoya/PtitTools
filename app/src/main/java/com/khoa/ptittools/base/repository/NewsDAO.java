package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khoa.ptittools.base.model.News;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface NewsDAO {

    @Insert
    void insertNews(News news);

    @Delete
    void deleteNews(News news);

    @Query("Select * from news")
    List<News> getAllNews();

    @Query("Delete from news")
    void deleteAllNews();
}
