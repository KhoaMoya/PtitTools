package com.khoa.ptittools.profile.util;

import com.khoa.ptittools.base.model.UserInfo;
import com.khoa.ptittools.base.util.ParseResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ParseResponseProfile extends ParseResponse {

    public static UserInfo convertToThongTin(Connection.Response response) {
        Document document = Jsoup.parse(response.body());
        UserInfo userInfo = new UserInfo();

        Element elementMaSV = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblMaSinhVien]").first();
        if (elementMaSV != null) userInfo.maSv = elementMaSV.text();

        Element elementTenSV = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblTenSinhVien]").first();
        if (elementTenSV != null) userInfo.ten = elementTenSV.text();

        Element elementNoiSinh = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblNoiSinh]").first();
        if (elementNoiSinh != null) userInfo.noisinh = elementNoiSinh.text();

        Element elementNgaySinh = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblNgaySinh]").first();
        if (elementNgaySinh != null) userInfo.ngaysinh = elementNgaySinh.text();

        Element elementLop = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblLop]").first();
        if (elementLop != null) userInfo.lop  = elementLop.text();

        Element elementNganh = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lbNganh]").first();
        if (elementNganh != null) userInfo.chuyennganh = elementNganh.text();

        Element elementKhoa = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblKhoa]").first();
        if (elementKhoa != null) userInfo.khoa = elementKhoa.text();

        Element elementHeDt = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblHeDaoTao]").first();
        if (elementHeDt != null) userInfo.hedaotao = elementHeDt.text();

        Element elementKhoaHoc = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblKhoaHoc]").first();
        if (elementKhoaHoc != null) userInfo.khoahoc = elementKhoaHoc.text();

        Element elementCvht = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblCVHT]").first();
        if (elementCvht != null) userInfo.covanhoctap = elementCvht.text();

        return userInfo;
    }

}
