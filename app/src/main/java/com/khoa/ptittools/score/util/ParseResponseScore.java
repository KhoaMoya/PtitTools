package com.khoa.ptittools.score.util;

import com.khoa.ptittools.base.model.SemesterScore;
import com.khoa.ptittools.base.model.SubjectScore;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ParseResponseScore {

    public static List<SemesterScore> convertToSemesterScore(Connection.Response response, String maSv) {

        ArrayList<SemesterScore> listSemesterScore = new ArrayList<SemesterScore>();
        Document document = Jsoup.parse(response.body());
        Element table = document.select("table[class=view-table]").first();

        Elements allElement = table.select("tr");
        SemesterScore diemHocKy = null;
        for (Element element : allElement) {
            if (element.toString().contains("class=\"title-hk-diem\"")) {
                if (diemHocKy != null) listSemesterScore.add(diemHocKy);
                diemHocKy = new SemesterScore(element.text(), maSv);
            }

            if (element.toString().contains("class=\"row-diem\"")) {
                Elements columns = element.select("td");
                SubjectScore diemMonHoc = new SubjectScore(columns.get(1).text().replace("\u00a0", ""),
                        columns.get(2).text().replace("\u00a0", ""),
                        columns.get(3).text().replace("\u00a0", ""),
                        columns.get(13).text().replace("\u00a0", ""),
                        columns.get(15).text().replace("\u00a0", ""),
                        columns.get(16).text().replace("\u00a0", ""));
                diemHocKy.subjectScoreList.add(diemMonHoc);
            }

            if (element.toString().contains("class=\"row-diemTK\"")) {
                Elements columns = element.select("span");
//                if(columns.get(0).text().equals("Điểm trung bình học kỳ hệ 10/100:")) diemHocKy.setDiemTB10(columns.get(1).text());
                if (columns.get(0).text().equals("Điểm trung bình học kỳ hệ 4:"))
                    diemHocKy.diemTB4 = columns.get(1).text();
//                if(columns.get(0).text().equals("Điểm trung bình tích lũy:")) diemHocKy.setDiemTBTichLuy10(columns.get(1).text());
                if (columns.get(0).text().equals("Điểm trung bình tích lũy (hệ 4):"))
                    diemHocKy.diemTBTichLuy4 = columns.get(1).text();
                if (columns.get(0).text().equals("Số tín chỉ đạt:"))
                    diemHocKy.soTinChiDat = columns.get(1).text();
                if (columns.get(0).text().equals("Số tín chỉ tích lũy:"))
                    diemHocKy.soTinChiTichLuy = columns.get(1).text();
            }
        }

        if (!listSemesterScore.contains(diemHocKy)) listSemesterScore.add(diemHocKy);

        return listSemesterScore;
    }
}
