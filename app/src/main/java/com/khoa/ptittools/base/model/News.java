package com.khoa.ptittools.base.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.khoa.ptittools.base.util.TimeUtil;

import java.io.Serializable;

@Entity
public class News implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "title")
    public String title = "";
    @ColumnInfo(name = "content")
    public String content = "";
    @ColumnInfo(name = "time")
    public String time = "";
    @ColumnInfo(name = "update_time")
    public String updateTime = "";
    @ColumnInfo(name = "link")
    public String link = "";
    @ColumnInfo(name = "url_image")
    public String imageUrl;
    @ColumnInfo(name = "summary")
    public String summary;

    public News() {
    }

    @Ignore
    public News(String title, String time, String updateTime, String link, String imageUrl, String summary) {
        this.id = link;
        this.title = title;
        this.time = time;
        this.updateTime = updateTime;
        this.link = link;
        this.imageUrl = imageUrl;
        this.summary = summary;
    }
}
