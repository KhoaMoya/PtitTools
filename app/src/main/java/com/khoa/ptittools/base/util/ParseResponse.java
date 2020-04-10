package com.khoa.ptittools.base.util;

/*
 * Created at 9/25/19 11:07 AM by Khoa
 */

import android.util.Log;

import com.khoa.ptittools.base.model.User;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;

public class ParseResponse {

    public static boolean checkLoginEmpty(Connection.Response response) {
        Document mDocument = Jsoup.parse(response.body());
        Element mElementUserName = mDocument.select("span[id=ctl00_Header1_ucLogout_lblNguoiDung]").first();

        Log.e("Loi", "Check login Empty|" + mElementUserName.text() + "|");
        return !mElementUserName.text().trim().equals("");
    }

    public static boolean checkLogin(Connection.Response response) {
        Document mDocument = Jsoup.parse(response.body());
        Element mElementUserName = mDocument.select("span[id=ctl00_Header1_ucLogout_lblNguoiDung]").first();

        Log.e("Loi", "Check login|" + mElementUserName.text() + "|");
        return !mElementUserName.text().trim().equals("Chào bạn");
    }

    public static String[] getCookieAndViewState(Connection.Response response) throws Exception {
        Document mDocument = Jsoup.parse(response.body());
        Element viewStateElm = mDocument.select("input[id=__VIEWSTATE]").first();
        String viewState = viewStateElm.val();
        if (viewState == null) viewState = "";

        Map<String, String> loginCookies = response.cookies();
        String cookie = loginCookies.get(User.mKeyCookie);
        if (cookie == null) cookie = "";

        return new String[]{cookie, viewState};
    }

    public static String getCapcha(Connection.Response response) {
        //<span id="ctl00_ContentPlaceHolder1_ctl00_lblCapcha"
        Document mDocument = Jsoup.parse(response.body());
        Element capchaElm = mDocument.select("span[id=ctl00_ContentPlaceHolder1_ctl00_lblCapcha]").first();
        if (capchaElm != null) {
            return capchaElm.text();
        } else {
            return "";
        }
    }
}
