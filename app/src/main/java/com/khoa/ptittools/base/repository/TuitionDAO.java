package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khoa.ptittools.base.model.Tuition;

@Dao
public interface TuitionDAO {
    @Query("select * from tuition where id = :id limit 1")
    Tuition getTuition(String id);

    @Delete
    void deleteTuition(Tuition tuition);

    @Insert
    void insertTuition(Tuition tuition);
}
