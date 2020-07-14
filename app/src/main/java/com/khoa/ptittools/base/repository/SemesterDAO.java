package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khoa.ptittools.base.model.Semester;

import java.util.List;

@Dao
public interface SemesterDAO {
    @Insert
    void insertSemester(Semester semester);

    @Delete
    void deleteSemester(Semester semester);

    @Update
    void updateSemester(Semester semester);

    @Query("Select * from semester where ma_sv =:userId and semester_code=:semesterCode LIMIT 1")
    Semester getSemester(String userId, String semesterCode);

    @Query("select count(id) from semester where id=:semesterId")
    int countSemester(String semesterId);

    @Query("select * from semester where ma_sv=:maSv order by semester_code desc")
    List<Semester> getAllSemesterOfUser(String maSv);

    @Query("delete from semester where ma_sv=:maSv")
    void deleteAllSemesterOfUser(String maSv);
}
