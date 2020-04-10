package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.khoa.ptittools.base.model.SemesterScore;

import java.util.List;

@Dao
public interface SemesterScoreDAO {

    @Query("select * from semesterscore where ma_sv = :maSv")
    List<SemesterScore> getSemesterScore(String maSv);

    @Query("delete from semesterscore where ma_sv = :maSv")
    void deleteAllScore(String maSv);

    @Insert
    void insertSemesterScore(SemesterScore semesterScore);

    @Delete
    void deleteSemesterScore(SemesterScore semesterScore);
}
