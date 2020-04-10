package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khoa.ptittools.base.model.Week;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface WeekDAO {
    @Query("Select * from week")
    List<Week> getAllWeek();

    @Query("Select * from week where semester_id = :semesterId")
    List<Week> getWeekInSemester(String semesterId);

    @Insert
    void insertWeek(Week week);

    @Update
    Completable updateWeek(Week week);

    @Delete
    void deleteWeek(Week week);

    @Query("Delete from week where ma_sv = :maSv")
    void deleteAllWeekOfUser(String maSv);
}
