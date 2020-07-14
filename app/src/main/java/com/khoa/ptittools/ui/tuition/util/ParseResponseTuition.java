package com.khoa.ptittools.ui.tuition.util;

import com.khoa.ptittools.base.model.Tuition;
import com.khoa.ptittools.base.model.SubjectTuition;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseResponseTuition {

    public static Tuition convertToTuiTion(Connection.Response response) {
        Tuition tuition = new Tuition();

        Document document = Jsoup.parse(response.body());

        // số tài khoản
        Element elementSoTK = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_lblSoTaiKhoan]").first();
        if (elementSoTK != null)
            tuition.soTaiKhoan = elementSoTK.text().substring(39, elementSoTK.text().length() - 1);

        Element table = document.select("table[id=ctl00_ContentPlaceHolder1_ctl00_gvHocPhi]").first();
        if (table != null) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                SubjectTuition subject = new SubjectTuition();
                Elements allColumn = row.select("td");
                if (allColumn.size() > 9) {
                    if (!allColumn.get(2).text().isEmpty()) {
                        subject.tenMonHoc = allColumn.get(2).text();
                        if (!allColumn.get(5).text().isEmpty())
                            subject.soTinChi = allColumn.get(5).text();
                        if (!allColumn.get(9).text().isEmpty())
                            subject.phaiDong = allColumn.get(9).text();
                        tuition.subjectList.add(subject);
                    }
                }
            }
        }

        Element elementTongSoTC = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_SoTinChiHP]").first();
        if (elementTongSoTC != null) tuition.tongSoTinChi = elementTongSoTC.text();

        Element elementTongSoTinChiHocPhi = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_SoTinChi]").first();
        if (elementTongSoTinChiHocPhi != null)
            tuition.tongSoTinChiHocPhi = elementTongSoTinChiHocPhi.text();

        Element elementTongSoTienHocPhiHocKy = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_lblphaiDong]").first();
        if (elementTongSoTienHocPhiHocKy != null)
            tuition.tongSoTienHocPhiHocKy = elementTongSoTienHocPhiHocKy.text();

        Element elementSoTienDongToiThieuLanDau = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_lblDongLanDau1]").first();
        if (elementSoTienDongToiThieuLanDau != null)
            tuition.soTienDongToiThieuLanDau = elementSoTienDongToiThieuLanDau.text();

        Element elementSoTienDaDongTrongHocKy = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_lblDaDongHKOffline]").first();
        if (elementSoTienDaDongTrongHocKy != null)
            tuition.soTienDaDongTrongHocKy = elementSoTienDaDongTrongHocKy.text();

        Element elementConNoHocKy = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_lblConNoHocKy]").first();
        if (elementConNoHocKy != null) tuition.soTienConNo = elementConNoHocKy.text();

        // last update span id="ctl00_ContentPlaceHolder1_ctl00_lblNote"
        Element elementLastUpdate = document.select("span[id=ctl00_ContentPlaceHolder1_ctl00_lblNote]").first();
        if (elementLastUpdate != null)
            tuition.lastUpdate = elementLastUpdate.text().substring(2, elementLastUpdate.text().length() - 2);
        return tuition;
    }
}
