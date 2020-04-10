package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khoa.ptittools.base.model.Semester;

@Dao
public interface SemesterDAO {
    @Insert
    void insertSemester(Semester semester);

    @Delete
    void deleteSemester(Semester semester);

    @Update
    void updateSemester(Semester semester);

    @Query("Select * from semester where ma_sv =:userId LIMIT 1")
    Semester getSemester(String userId);
}
