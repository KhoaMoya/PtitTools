package com.khoa.ptittools.base.net;

public interface ScheduleDownloaderListener {

    boolean isCanceled();

    void onUpdateProgress(String message, int progress);

}
