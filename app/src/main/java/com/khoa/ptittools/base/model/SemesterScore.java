package com.khoa.ptittools.base.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import com.khoa.recycleviewexpandable.Model.ParentListItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SemesterScore implements Serializable, ParentListItem {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "ten_hoc_ky")
    public String tenHocKy;
    @ColumnInfo(name = "subject_score_list")
    public List<SubjectScore> subjectScoreList;
    @ColumnInfo(name = "diem_tb10")
    public String diemTB10;
    @ColumnInfo(name = "dien_tb4")
    public String diemTB4;
    @ColumnInfo(name = "diem_tb_tl10")
    public String diemTBTichLuy10;
    @ColumnInfo(name = "diem_tb_tl4")
    public String diemTBTichLuy4;
    @ColumnInfo(name = "so_tin_chi_dat")
    public String soTinChiDat;
    @ColumnInfo(name = "so_tin_chi_tl")
    public String soTinChiTichLuy;

    @ColumnInfo(name = "ma_sv")
    public String maSv;

    public SemesterScore() {
    }

    @Ignore
    public SemesterScore(String tenHocKy, String maSv) {
        this.tenHocKy = tenHocKy;
        this.maSv = maSv;
        subjectScoreList = new ArrayList<>();
    }

    @Ignore
    @Override
    public List<?> getChildItemList() {
        return subjectScoreList;
    }

    @Ignore
    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
