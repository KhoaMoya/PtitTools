package com.khoa.ptittools.base.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.khoa.ptittools.base.util.TimeUtil;

@Entity
public class Exam {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "ma_mon")
    public String maMon;
    @ColumnInfo(name = "ten_mon")
    public String tenMon;
    @ColumnInfo(name = "ghep_thi")
    public String ghepThi;
    @ColumnInfo(name = "to_thi")
    public String toThi;
    @ColumnInfo(name = "so_luong")
    public String soLuong;
    @ColumnInfo(name = "ngay_thi")
    public String ngayThi;
    @ColumnInfo(name = "gio_bat_dau")
    public String gioBD;
    @ColumnInfo(name = "so_phut")
    public String soPhut;
    @ColumnInfo(name = "phong")
    public String phong;
    @ColumnInfo(name = "ghi_chu")
    public String ghiChu;
    @ColumnInfo(name = "last_update")
    public String lastUpdate;
    @ColumnInfo(name = "ma_sv")
    public String maSv;

    public Exam() {
    }

    @Ignore
    public Exam(String maMon, String tenMon, String ghepThi, String toThi, String soLuong, String ngayThi, String gioBD, String soPhut, String phong, String ghiChu, String maSv) {
        this.id = maSv + "_" + maMon + "_" + ngayThi + "_" + gioBD;
        this.lastUpdate = TimeUtil.getTimeCurrent();
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.ghepThi = ghepThi;
        this.toThi = toThi;
        this.soLuong = soLuong;
        this.ngayThi = ngayThi;
        this.gioBD = gioBD;
        this.soPhut = soPhut;
        this.phong = phong;
        this.ghiChu = ghiChu;
        this.maSv = maSv;
    }
}
