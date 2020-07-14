package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khoa.ptittools.base.model.Exam;

import java.util.List;

@Dao
public interface ExamDAO {
    @Query("select * from exam where ma_sv = :maSV")
    List<Exam> getExamList(String maSV);

    @Query("delete from exam where ma_sv =:maSV")
    void deleteOldExam(String maSV);

    @Delete
    void deleteExam(Exam exam);

    @Insert
    void insertExam(Exam exam);
}
