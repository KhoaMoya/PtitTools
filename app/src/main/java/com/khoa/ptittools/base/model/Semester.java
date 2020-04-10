package com.khoa.ptittools.base.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Semester {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id = "";
    @ColumnInfo(name = "semester_code")
    public String semesterCode = "";
    @ColumnInfo(name = "semester_name")
    public String semesterName = "";
    @ColumnInfo(name = "last_update")
    public String lastUpdate = "";
    @ColumnInfo(name = "ma_sv")
    public String maSv = "";

    @Ignore
    public List<Week> weekList;

    public Semester() {
        this.weekList = new ArrayList<>();
    }

    @Ignore
    public Semester(String semesterCode, String semesterName, String maSv) {
        this.id = semesterCode + maSv;
        this.semesterCode = semesterCode;
        this.semesterName = semesterName;
        this.maSv = maSv;
        this.weekList = new ArrayList<>();
    }
}
