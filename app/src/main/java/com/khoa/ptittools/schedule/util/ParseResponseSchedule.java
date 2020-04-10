package com.khoa.ptittools.schedule.util;

import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.model.Subject;
import com.khoa.ptittools.base.model.Week;
import com.khoa.ptittools.base.util.ParseResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ParseResponseSchedule extends ParseResponse {

    public static List<Week> getWeekList(Connection.Response response, String semesterCode, String maSv) {
        List<Week> listWeek = new ArrayList<>();
        Document document = Jsoup.parse(response.body());

        Element select = document.select("select[id=ctl00_ContentPlaceHolder1_ctl00_ddlTuan]").first();
        Elements options = select.select("option");
        for (Element op : options) {
//            Tuần 01 [Từ 12/08/2019 -- Đến 18/08/2019]
//            01234567890123456789012345678901234567890
            String string = op.text();
            String tenTuan = string.substring(0, 7);
            String ngayBD = string.substring(12, 22);
            String ngayKT = string.substring(29, 40);
            String value = op.val();
            Week week = new Week(tenTuan, value, ngayBD, ngayKT, semesterCode, maSv);

            String selected = op.attr("selected");
            if(selected!=null && selected.equals("selected")) {
                List<Subject> subjectList = getSubjectList(response, week.id, maSv);
                week.subjectList = subjectList;
            }

            listWeek.add(week);
        }
        return listWeek;
    }



    public static Semester getSemester(Connection.Response response, String maSv) {
        Document document = Jsoup.parse(response.body());
        Element selectHocKy = document.select("select[id=ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK]").first();
        Element selectedHocKy = selectHocKy.select("option[selected]").first();
        return new Semester(selectedHocKy.val(), selectedHocKy.text(), maSv);
    }

    public static List<Subject> getSubjectList(Connection.Response response, String weekId, String maSv) {
        List<Subject> subjects = new ArrayList<>();
        Document document = Jsoup.parse(response.body());

        Element table = document.select("table[id=ctl00_ContentPlaceHolder1_ctl00_Table1]").first();
        Elements kips = table.select("td[onmouseover]");

        String[] arrThu = {"Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy", "Chủ Nhật"};
        List<String> listThu = new ArrayList<>(Arrays.asList(arrThu));

        for (Element kip : kips) {
            String content = kip.attr("onmouseover");
            content = content.replace("ddrivetip(", "");
            content = content.replace(")", "");
            String[] arr = content.split(",");
            Subject subject = new Subject();
            subject.classCode = arr[0].replace("'", "");
            subject.subjectName = arr[1].replace("'", "");
            subject.subjectCode = arr[2].replace("'", "");
            subject.day = listThu.indexOf(arr[3].replace("'", "")) + 2;
            subject.soTinChi = arr[4].replace("'", "");
            subject.roomName = arr[5].replace("'", "");
            subject.startLesson = Integer.valueOf(arr[6].replace("'", ""));
            subject.durationLesson = Integer.valueOf(arr[7].replace("'", ""));
            subject.teacher = arr[8].replace("'", "");
//            subject.startDate = arr[9].replace("'", "");
//            subject.endDate = arr[10].replace("'", "");
            subject.weekId = weekId;
            subject.maSv = maSv;

            subjects.add(subject);
        }
        return subjects;
    }


    public static int getCurrentWeekIndex(List<Week> list) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date currentDate = formatter.parse(formatter.format(new Date()));

            for (int i = 0; i < list.size(); i++) {
                Week week = list.get(i);
                Date bdDate = formatter.parse(week.startDate);
                Date ktDate = formatter.parse(week.endDate);
                if (currentDate.compareTo(bdDate) >= 0 && currentDate.compareTo(ktDate) <= 0) {
                    return i;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
