package com.khoa.ptittools.news.util;

import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.util.ParseResponse;
import com.khoa.ptittools.base.util.TimeUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;


public class ParseResponseNews extends ParseResponse {


    public static List<News> convertToNews(Connection.Response response) throws Exception{
        Document mDocument = response.parse();
        Element postGroupElm = mDocument.getElementsByClass("posts_group lm_wrapper classic col-3").first();
        List<News> newsList = new ArrayList<>();
        for (int j = 0; j < postGroupElm.children().size(); j++) {
            Element postElm = postGroupElm.child(j);

            Element timeElm = postElm.getElementsByClass("date_label").first();
            String time = timeElm.text();

            Element imgElm = postElm.getElementsByTag("img").first();
            String imgUrl = imgElm.attr("src");

            Element linkElm = postElm.getElementsByTag("a").first();
            String link = linkElm.attr("href");

            Element titleDivElm = postElm.getElementsByClass("post-title").first();
            Element titleElm = titleDivElm.getElementsByTag("a").first();
            String title = titleElm.text();

            Element summaryElm = postElm.getElementsByClass("post-excerpt").first();
            String summary = summaryElm.text();

            newsList.add(new News(title, time, TimeUtil.getTimeCurrent(), link, imgUrl, summary));
        }
        return newsList;
    }

    public static String convertNewsContent(Connection.Response response){
        Document document = Jsoup.parse(response.body());
        Element contentElm = document.getElementsByClass("section the_content has_content").first();
        String content = contentElm.html();
        return "<html>" + content + "</html>";
    }
}
