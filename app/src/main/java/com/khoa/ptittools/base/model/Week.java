package com.khoa.ptittools.base.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Week implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id = "";
    @ColumnInfo(name = "week_name")
    public String weekName;
    @ColumnInfo(name = "value")
    public String value;
    @ColumnInfo(name = "start_date")
    public String startDate;
    @ColumnInfo(name = "end_date")
    public String endDate;

    @ColumnInfo(name = "semester_id")
    public String semesterId;

    @ColumnInfo(name = "ma_sv")
    public String maSv;

    @Ignore
    public List<Subject> subjectList;

    public Week() {
        subjectList = new ArrayList<>();
    }

    @Ignore
    public Week(String weekName, String value, String startDate, String endDate, String semesterId, String maSv) {
        this.weekName = weekName;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = value + "_" + semesterId;
        this.semesterId = semesterId;
        this.maSv = maSv;
        subjectList = new ArrayList<>();
    }

    @Ignore
    @Override
    public String toString() {
        return weekName + ": " + startDate + " - " + endDate;
    }
}
