package com.khoa.scheduleview;

import android.graphics.Canvas;

import java.io.Serializable;

public class SimpleSubject{

    public int day;
    public int startLesson;
    public int durationLesson;
    public String subjectName;
    public String roomName;

    public LabelSubject labelSubject;

    public SimpleSubject() {
    }

    public SimpleSubject(int day, int startLesson, int durationLesson, String subjectName, String roomName) {
        this.day = day;
        this.startLesson = startLesson;
        this.durationLesson = durationLesson;
        this.subjectName = subjectName;
        this.roomName = roomName;
    }

    public void draw(Canvas canvas) {
        labelSubject.draw(canvas);
    }
}
