package com.khoa.scheduleview;

public interface OnScheduleClickListener {
    public void onClickSubject(int subjectIndex);
    public void onLongClickSubject(int subjectIndex);
    public void onClickAddEvent(int day, int startLesson);
}
