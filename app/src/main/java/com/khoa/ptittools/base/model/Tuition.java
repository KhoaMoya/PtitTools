package com.khoa.ptittools.base.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Tuition {

    @PrimaryKey
    @NonNull
    public String id;
    @ColumnInfo(name = "tong_so_tin_chi")
    public String tongSoTinChi;
    @ColumnInfo(name = "tong_so_tin_chi_hoc_phi")
    public String tongSoTinChiHocPhi;
    @ColumnInfo(name = "tong_so_tien_hoc_phi_hoc_ky")
    public String tongSoTienHocPhiHocKy;
    @ColumnInfo(name = "so_tien_dong_toi_thieu_lan_dau")
    public String soTienDongToiThieuLanDau;
    @ColumnInfo(name = "so_tien_da_dong_trong_hoc_ky")
    public String soTienDaDongTrongHocKy;
    @ColumnInfo(name = "so_tien_con_no")
    public String soTienConNo;
    @ColumnInfo(name = "subject_list")
    public List<SubjectTuition> subjectList;
    @ColumnInfo(name = "last_update")
    public String lastUpdate;
    @ColumnInfo(name = "so_tai_khoan")
    public String soTaiKhoan;

    public Tuition() {
        this.subjectList = new ArrayList<>();
    }

    @Ignore
    public Tuition(@NonNull String id, String tongSoTinChi, String tongSoTinChiHocPhi, String tongSoTienHocPhiHocKy, String soTienDongToiThieuLanDau, String soTienDaDongTrongHocKy, String soTienConNo, List<SubjectTuition> subjectList, String lastUpdate, String soTaiKhoan) {
        this.id = id;
        this.tongSoTinChi = tongSoTinChi;
        this.tongSoTinChiHocPhi = tongSoTinChiHocPhi;
        this.tongSoTienHocPhiHocKy = tongSoTienHocPhiHocKy;
        this.soTienDongToiThieuLanDau = soTienDongToiThieuLanDau;
        this.soTienDaDongTrongHocKy = soTienDaDongTrongHocKy;
        this.soTienConNo = soTienConNo;
        this.subjectList = subjectList;
        this.lastUpdate = lastUpdate;
        this.soTaiKhoan = soTaiKhoan;
    }
}
