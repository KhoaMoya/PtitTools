package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khoa.ptittools.base.model.Subject;

import java.util.List;

@Dao
public interface SubjectDAO {
    @Query("Select * from subject")
    List<Subject> getAllSubjects();

    @Query("Select * from subject Where private = 1")
    List<Subject> getPrivateSubject();

    @Query("Select * from subject Where week_id = :weekId")
    List<Subject> getSubjectInWeek(String weekId);

    @Insert
    void insertSubject(Subject subject);

    @Query("Delete from subject where private = 0 and ma_sv = :maSv")
    void deleteSubjectNotPrivateOfUser(String maSv);

    @Delete
    void deleteSubject(Subject subject);

    @Update
    void updateSubject(Subject subject);
}
