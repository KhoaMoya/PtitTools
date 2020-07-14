package com.khoa.ptittools.ui.exam.util;

import com.khoa.ptittools.base.model.Exam;
import com.khoa.ptittools.base.util.ParseResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ParseResponseExam extends ParseResponse {

    public static List<Exam> convertToExamList(Connection.Response response, String maSv) throws Exception {
        List<Exam> examList = new ArrayList<>();
        if (response == null) return examList;
        Document document = Jsoup.parse(response.body());

        Element tableElm = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_gvXem");
        Elements rowsElm = tableElm.getElementsByTag("tr");
        for (int i = 1; i < rowsElm.size(); i++) {
            Element rowElm = rowsElm.get(i);
            Elements columnsElm = rowElm.getElementsByTag("td");
            String maMon = columnsElm.get(1).text();
            String tenMon = columnsElm.get(2).text();
            String ghepThi = columnsElm.get(3).text();
            String toThi = columnsElm.get(4).text();
            String soLuong = columnsElm.get(5).text();
            String ngayThi = columnsElm.get(6).text();
            String gioBd = columnsElm.get(7).text();
            String soPhut = columnsElm.get(8).text();
            String phong = columnsElm.get(9).text();
            String ghiChu = columnsElm.get(10).text();

//            String maMon, String tenMon, String ghepThi, String toThi, String soLuong, String ngayThi, String gioBD, String soPhut, String phong, String ghiChu, String maSv
            examList.add(new Exam(maMon, tenMon, ghepThi, toThi, soLuong, ngayThi, gioBd, soPhut, phong, ghiChu, maSv));
        }

        return examList;
    }
}
