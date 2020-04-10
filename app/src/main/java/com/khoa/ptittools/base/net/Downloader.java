package com.khoa.ptittools.base.net;

import android.content.Context;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.Exam;
import com.khoa.ptittools.base.model.MyException;
import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.model.SemesterScore;
import com.khoa.ptittools.base.model.Subject;
import com.khoa.ptittools.base.model.Tuition;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.model.Week;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.base.util.ParseResponse;
import com.khoa.ptittools.base.util.TimeUtil;
import com.khoa.ptittools.exam.util.ParseResponseExam;
import com.khoa.ptittools.news.util.ParseResponseNews;
import com.khoa.ptittools.schedule.util.ParseResponseSchedule;
import com.khoa.ptittools.score.util.ParseResponseScore;
import com.khoa.ptittools.tuition.util.ParseResponseTuition;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Downloader {

    private static Context getContext(){
        return MyApplication.getContext();
    }

    public static Connection.Response getHtml(String url, String cookie) {
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url)
                    .cookie("ASP.NET_SessionId", cookie)
                    .header("Connection", "keep-alive")
                    .method(Connection.Method.GET)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Connection.Response postLogin(User user) throws Exception {
        Connection.Response response = null;
        response = Jsoup.connect(PTIT_URL.Login_URL)
                .data("__EVENTTARGET", "")
                .data("__EVENTARGUMENT", "")
                .data("__VIEWSTATE", user.viewState)
                .data("__VIEWSTATEGENERATOR", "CA0B0334")
                .data("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtTaiKhoa", user.maSV)
                .data("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtMatKhau", user.matKhau)
                .data("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$btnDangNhap", "Đăng Nhập")
                .header("Connection", "keep-alive")
                .cookie("ASP.NET_SessionId", user.cookie)
                .method(Connection.Method.POST)
                .timeout(10000)
                .execute();
        return response;
    }

    public static Connection.Response postScheduleWeek(User user, String url, String semesterCode, String weekValue) {
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url)
                    .data("ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK", semesterCode)
                    .data("ctl00$ContentPlaceHolder1$ctl00$ddlLoai", "0")
                    .data("ctl00$ContentPlaceHolder1$ctl00$ddlTuan", weekValue)
                    .data("__EVENTTARGET", "ctl00$ContentPlaceHolder1$ctl00$ddlTuan")
                    .data("__EVENTARGUMENT", "")
                    .data("__LASTFOCUS", "")
                    .data("__VIEWSTATE", user.viewState)
                    .data("__VIEWSTATEGENERATOR", "CA0B0334")
                    .cookie("ASP.NET_SessionId", user.cookie)
                    .header("Connection", "keep-alive")
                    .method(Connection.Method.POST)
                    .timeout(10000)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static User login(User user) throws Exception {
        String url = "http://qldt.ptit.edu.vn/Default.aspx?page=gioithieu";

        Connection.Response response = Downloader.postLogin(user);
        user.setCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        boolean logedIn = ParseResponse.checkLoginEmpty(response);

        if (logedIn) {
            return user;
        }

        response = Downloader.getHtml(url, user.cookie);
        user.setCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        String capcha = ParseResponse.getCapcha(response);

        if (capcha.isEmpty()) {
            throw new MyException("Thông tin đăng nhập không chính xác");
        }

        response = Jsoup.connect(url)
                .data("__EVENTTARGET", "")
                .data("__EVENTARGUMENT", "")
                .data("__VIEWSTATE", user.viewState)
                .data("__VIEWSTATEGENERATOR", "CA0B0334")
                .data("ctl00$ContentPlaceHolder1$ctl00$txtCaptcha", capcha)
                .data("ctl00$ContentPlaceHolder1$ctl00$btnXacNhan", "Vào website")
                .cookie("ASP.NET_SessionId", user.cookie)
                .header("Connection", "keep-alive")
                .method(Connection.Method.POST)
                .timeout(10000)
                .execute();
        user.setCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        Document mDocument = Jsoup.parse(response.body());
        Element loginElm = mDocument.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_btnDangNhap");

        if (loginElm == null) {
            throw new MyException("Lỗi. Vượt capcha thất bại");
        }

        response = Downloader.postLogin(user);
        user.setCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        logedIn = ParseResponse.checkLogin(response);
        if (!logedIn) {
            throw new MyException("Thông tin đăng nhập không chính xác");
        }

        return user;
    }

    public static Connection.Response login() throws Exception {
        String url = "http://qldt.ptit.edu.vn/Default.aspx?page=gioithieu";

        AppRepository appRepository = MyApplication.getAppRepository();
        User user = appRepository.takeUser();
        if(user.maSV.isEmpty()) throw new Exception("Người dùng không xác định");

        Connection.Response response = getHtml(url, user.cookie);
        appRepository.updateCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        user = appRepository.takeUser();
        response = Downloader.postLogin(user);
        appRepository.updateCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        boolean logedIn = ParseResponse.checkLogin(response);

        if (logedIn) {
            return response;
        }
        user = appRepository.takeUser();
        response = Downloader.getHtml(url, user.cookie);
        appRepository.updateCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        String capcha = ParseResponse.getCapcha(response);

        if (capcha.isEmpty()) {
            throw new MyException("Thông tin đăng nhập không chính xác");
        }

        user = appRepository.takeUser();
        response = Jsoup.connect(url)
                .data("__EVENTTARGET", "")
                .data("__EVENTARGUMENT", "")
                .data("__VIEWSTATE", user.viewState)
                .data("__VIEWSTATEGENERATOR", "CA0B0334")
                .data("ctl00$ContentPlaceHolder1$ctl00$txtCaptcha", capcha)
                .data("ctl00$ContentPlaceHolder1$ctl00$btnXacNhan", "Vào website")
                .cookie("ASP.NET_SessionId", user.cookie)
                .header("Connection", "keep-alive")
                .method(Connection.Method.POST)
                .timeout(10000)
                .execute();
        appRepository.updateCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        Document mDocument = Jsoup.parse(response.body());
        Element loginElm = mDocument.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucDangNhap_btnDangNhap");

        if (loginElm == null) {
            throw new MyException("Lỗi. Vượt capcha thất bại");
        }

        user = appRepository.takeUser();
        response = Downloader.postLogin(user);
        appRepository.updateCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        logedIn = ParseResponse.checkLogin(response);
        if (!logedIn) throw new MyException("Thông tin đăng nhập không chính xác");

        return response;
    }

    public static Connection.Response getHtml(String url) throws Exception {
        AppRepository appRepository = MyApplication.getAppRepository();
        User user = appRepository.takeUser();
        if(user.maSV.isEmpty()) throw new Exception("Người dùng không xác định");

        Connection.Response response = Downloader.getHtml(url, user.cookie);
        appRepository.updateCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        boolean logedIn = ParseResponse.checkLogin(response);
        if (logedIn) {
            return response;
        }

        login();

        user = appRepository.takeUser();
        response = Downloader.getHtml(url, user.cookie);
        appRepository.updateCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        return response;
    }

    public static List<SemesterScore> downloadAllScore() throws Exception{
        getHtml(PTIT_URL.Score_URL);

        AppRepository appRepository = MyApplication.getAppRepository();
        User user = appRepository.takeUser();
        if(user.maSV.isEmpty()) throw new Exception("Người dùng không xác định");

        Connection.Response response = Jsoup.connect(PTIT_URL.Score_URL)
                .data("__EVENTTARGET", "ctl00$ContentPlaceHolder1$ctl00$lnkChangeview2")
                .data("__EVENTARGUMENT", "")
                .data("__VIEWSTATE", user.viewState)
                .data("__VIEWSTATEGENERATOR", "CA0B0334")
                .cookie("ASP.NET_SessionId", user.cookie)
                .header("Connection", "keep-alive")
                .method(Connection.Method.POST)
                .timeout(10000)
                .execute();

        appRepository.updateCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        List<SemesterScore> list = ParseResponseScore.convertToSemesterScore(response, user.maSV);

        return list;
    }

    public static Semester downloadFirstPage() throws Exception{
        AppRepository appRepository = MyApplication.getAppRepository();
        User user = appRepository.takeUser();
        if(user.maSV.isEmpty()) throw new Exception("Người dùng không xác định");

        String url = PTIT_URL.Schedule_URL + user.maSV;
        Connection.Response response = Downloader.getHtml(url, user.cookie);
        appRepository.updateCookieAndViewState(ParseResponse.getCookieAndViewState(response));

        // set semester
        user = appRepository.takeUser();
        Semester semester = ParseResponseSchedule.getSemester(response, user.maSV);
        List<Week> weekList = ParseResponseSchedule.getWeekList(response, semester.id, user.maSV);

        semester.weekList = weekList;

        return semester;
    }

    public static Semester downloadScheduleInWeeks(Semester semester, ScheduleDownloaderListener listener) throws Exception{
        AppRepository appRepository = MyApplication.getAppRepository();
        User user = appRepository.takeUser();
        if(user.maSV.isEmpty()) throw new Exception("Người dùng không xác định");

        String url = PTIT_URL.Schedule_URL + user.maSV;

        for (int i = 0; i < semester.weekList.size(); i++) {
            String message = "Đang tải dữ liệu " + semester.weekList.get(i).weekName;
            int progress = i * 100 / (semester.weekList.size() - 1);
            listener.onUpdateProgress(message, progress);

            user = appRepository.takeUser();
            Week week = semester.weekList.get(i);

            if(week.subjectList.isEmpty()) {
                Connection.Response response = Downloader.postScheduleWeek(user, url, semester.semesterCode, week.value);
                if (listener.isCanceled()) return semester;

                List<Subject> subjects = ParseResponseSchedule.getSubjectList(response, week.id, semester.maSv);
                semester.weekList.get(i).subjectList = subjects;

                // update cookie and viewSate
                appRepository.updateCookieAndViewState(ParseResponseSchedule.getCookieAndViewState(response));
            }
        }
        semester.lastUpdate = TimeUtil.getTimeCurrent();
        semester.maSv = appRepository.takeUser().maSV;

        return semester;
    }

    public static Semester downloadSemester(ScheduleDownloaderListener listener) throws Exception{
        Semester semester = downloadFirstPage();
        semester = downloadScheduleInWeeks(semester, listener);
        return semester;
    }

    public static List<News> downloadNewsList(int amountPage) throws Exception{
        List<News> newsList = new ArrayList<>();
        for (int i = 1; i <= amountPage; i++) {
            Connection.Response response = Downloader.getHtml(PTIT_URL.News_URL + i, "");
            newsList.addAll(ParseResponseNews.convertToNews(response));
        }
        return newsList;
    }

    public static Tuition downloadTuition() throws Exception {
        User user = MyApplication.getAppRepository().takeUser();
        if(user.maSV.isEmpty()) throw new Exception("Người dùng không xác định");

        Connection.Response response = Downloader.getHtml(PTIT_URL.Tuition_URL);
        Tuition tuition = ParseResponseTuition.convertToTuiTion(response);
        tuition.id = user.maSV;
        return tuition;
    }

    public static List<Exam> downloadExamList() throws Exception{
        User user = MyApplication.getAppRepository().takeUser();
        if(user.maSV.isEmpty()) throw new Exception("Người dùng không xác định");

        Connection.Response response = getHtml(PTIT_URL.Exam_URL);
        List<Exam> examList = ParseResponseExam.convertToExamList(response, user.maSV);
        return examList;
    }
}
