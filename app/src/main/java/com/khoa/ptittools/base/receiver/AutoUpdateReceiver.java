package com.khoa.ptittools.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.R;
import com.khoa.ptittools.base.helper.NotificationHelper;
import com.khoa.ptittools.base.model.Exam;
import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.model.SemesterScore;
import com.khoa.ptittools.base.model.Tuition;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.net.ScheduleDownloaderListener;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.base.util.CompareUtil;
import com.khoa.ptittools.score.view.ScoreActivity;
import com.khoa.ptittools.tuition.view.TuitionActivity;

import java.util.List;

public class AutoUpdateReceiver extends BroadcastReceiver implements ScheduleDownloaderListener {

    private AppRepository appRepository;
    private NotificationHelper notificationHelper;

    private boolean isShowNotification;
    private News news;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(context.getString(R.string.auto_update))) {
            // to do download and compare data
            this.appRepository = MyApplication.getAppRepository();
            this.notificationHelper = MyApplication.getNotificationHelper();
            this.isShowNotification = appRepository.ishowNotification();

            showCountUpdateTimeNotification();

            new Thread(getWorker()).start();
        }
    }

    private Runnable getWorker() {
        return new Runnable() {
            @Override
            public void run() {
                updateSchedule();
                updateExam();
                updateNews();
                updateTuition();
                updateScore();
            }
        };
    }

    private void updateSchedule() {
        try {
            Semester semester = Downloader.downloadSemester(this);
            boolean diffSchedule = CompareUtil.compareSchedule(semester);

            if (diffSchedule) {
                appRepository.updateSemester(semester);
                if(isShowNotification) showScheduleNotification();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateExam() {
        try {
            List<Exam> examList = Downloader.downloadExamList();
            boolean diffExam = CompareUtil.compareExam(examList);
            if (diffExam) {
                appRepository.updateExamList(examList);
                if(isShowNotification) showExamNotification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateNews() {
        try {
            List<News> newsList = Downloader.downloadNewsList(2);
            news = CompareUtil.compareNewsList(newsList);
            boolean diffNews = news != null;

            if (diffNews) {
                appRepository.updateNews(newsList);
                if(isShowNotification) showNewsNotification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTuition() {
        try {
            Tuition tuition = Downloader.downloadTuition();
            boolean diffTuition = CompareUtil.compareTuition(tuition);

            if (diffTuition) {
                appRepository.updateTuition(tuition);
                if(isShowNotification) showTuitionNotification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateScore() {
        try {
            List<SemesterScore> scoreList = Downloader.downloadAllScore();
            boolean diffScore = CompareUtil.compareScoreList(scoreList);

            if (diffScore) {
                appRepository.updateScore(scoreList);
                if(isShowNotification) showScoreNotification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showScheduleNotification() {
        NotificationCompat.Builder builder = notificationHelper.getNotification("Thời khóa biểu", "Nhấn để xem thời khóa biểu mới");
        notificationHelper.getNotificationManager().notify(NotificationHelper.NEW_SCHEDULE_NOTIFICATION_ID, builder.build());
    }

    private void showExamNotification() {
        NotificationCompat.Builder builder = notificationHelper.getExamNotification("Lịch thi", "Nhấn để xem lịch thi mới");
        notificationHelper.getNotificationManager().notify(NotificationHelper.NEW_EXAM_NOTIFICATION_ID, builder.build());
    }

    private void showNewsNotification() {
        if (news != null) notificationHelper.getNewsNotification(news);
    }

    private void showTuitionNotification() {
        String title = "Học phí";
        String content = "Nhấn để xem học phí mới";
        NotificationCompat.Builder builder = notificationHelper
                .getChildActivityNotification(NotificationHelper.NEW_TUITION_PENDING_INTENT_ID, title, content, TuitionActivity.class);
        notificationHelper.getNotificationManager().notify(NotificationHelper.NEW_TUITION_NOTIFICATION_ID, builder.build());
    }

    private void showScoreNotification() {
        String title = "Điểm thi";
        String content = "Nhấn để xem điểm thi mới";
        NotificationCompat.Builder builder = notificationHelper
                .getChildActivityNotification(NotificationHelper.NEW_SCORE_PENDING_INTENT_ID, title, content, ScoreActivity.class);
        notificationHelper.getNotificationManager().notify(NotificationHelper.NEW_SCORE_NOTIFICATION_ID, builder.build());
    }

    private void showCountUpdateTimeNotification() {
        NotificationCompat.Builder builder = notificationHelper.getCountNotification();
        notificationHelper.getNotificationManager().notify(0, builder.build());
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void onUpdateProgress(String message, int progress) {
        Log.e("Loi", message);
    }
}
