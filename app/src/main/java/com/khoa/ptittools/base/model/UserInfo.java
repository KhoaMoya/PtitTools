package com.khoa.ptittools.base.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserInfo {

    @PrimaryKey
    @NonNull
    public String maSv;
    @ColumnInfo(name = "ten")
    public String ten = "";
    @ColumnInfo(name = "imageurl")
    public String imageUrl = "";
    @ColumnInfo(name = "noisinh")
    public String noisinh = "";
    @ColumnInfo(name = "ngaysinh")
    public String ngaysinh = "";
    @ColumnInfo(name = "lop")
    public String lop = "";
    @ColumnInfo(name = "chuyennganh")
    public String chuyennganh = "";
    @ColumnInfo(name = "khoa")
    public String khoa = "";
    @ColumnInfo(name = "hedaotao")
    public String hedaotao = "";
    @ColumnInfo(name = "khoahoc")
    public String khoahoc = "";
    @ColumnInfo(name = "covanhoctap")
    public String covanhoctap = "";
    @ColumnInfo(name = "status")
    public String status = "";
    @ColumnInfo(name = "lastonline")
    public String lastonline = "";

    public UserInfo() {
    }
}
