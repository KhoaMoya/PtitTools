package com.khoa.ptittools.base.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Subject implements Serializable, Comparable<Subject> {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "day")
    public int day;
    @ColumnInfo(name = "start_lesson")
    public int startLesson;
    @ColumnInfo(name = "duration_lesson")
    public int durationLesson;
    @ColumnInfo(name = "subject_name")
    public String subjectName;
    @ColumnInfo(name = "room_name")
    public String roomName;
    @ColumnInfo(name = "subject_code")
    public String subjectCode;
    @ColumnInfo(name = "class_code")
    public String classCode;
    @ColumnInfo(name = "so_tin_chi")
    public String soTinChi;
    @ColumnInfo(name = "teacher")
    public String teacher;
    @ColumnInfo(name = "start_date")
    public String startDate="";
    @ColumnInfo(name = "end_date")
    public String endDate="";
    @ColumnInfo(name = "tuition")
    public String tuition;

    @ColumnInfo(name = "private")
    public int isPrivate = 0;

    @ColumnInfo(name = "week_id")
    public String weekId;
    @ColumnInfo(name="semester_id")
    public String semesterId;

    @ColumnInfo(name = "ma_sv")
    public String maSv;

    public Subject() {
    }

    @Ignore
    @Override
    public int compareTo(Subject subject) {
        if(day > subject.day) return 1;
        else if(day < subject.day) return -1;
        else return 0;
    }
}
