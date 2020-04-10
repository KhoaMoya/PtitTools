package com.khoa.ptittools.base.model;

import java.io.Serializable;

public class SubjectScore implements Serializable {

    public String maMonHoc;
    public String tenMonHoc;
    public String soTinChi;
    public String thi;
    public String TK10;
    public String TK4;

    public SubjectScore(String maMonHoc, String tenMonHoc, String soTinChi, String thi, String TK10, String TK4) {
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.soTinChi = soTinChi;
        this.thi = thi;
        this.TK10 = TK10;
        this.TK4 = TK4;
    }
}
