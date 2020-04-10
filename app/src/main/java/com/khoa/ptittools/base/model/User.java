package com.khoa.ptittools.base.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {

    public final static String mKeyCookie = "ASP.NET_SessionId";

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "masv")
    public String maSV = "";
    @ColumnInfo(name = "matkhau")
    public String matKhau = "";
    @ColumnInfo(name = "ten")
    public String ten;

    @Ignore
    public String cookie = "";
    @Ignore
    public String viewState = "";

    public User() {
    }

    @Ignore
    public User(@NonNull String maSV, String matKhau, String ten) {
        this.maSV = maSV.toUpperCase();
        this.matKhau = matKhau;
        this.ten = ten;
    }

    @Ignore
    public User(@NonNull String maSV, String matKhau, String cookie, String viewState) {
        this.maSV = maSV;
        this.matKhau = matKhau;
        this.cookie = cookie;
        this.viewState = viewState;
    }

    @Ignore
    public void setCookieAndViewState(String[] values){
        this.cookie = values[0];
        this.viewState = values[1];
    }
}
