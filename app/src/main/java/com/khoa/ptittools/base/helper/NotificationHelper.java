package com.khoa.ptittools.base.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.R;
import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.base.receiver.AutoUpdateReceiver;
import com.khoa.ptittools.exam.view.ExamFragment;
import com.khoa.ptittools.main.view.MainActivity;
import com.khoa.ptittools.news.view.DetailNewsActivity;
import com.khoa.ptittools.news.view.NewsFragment;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationHelper {

    public final static String NEW_DATA_CHANNEL_ID = "auto_update_channel_id";
    public final static String NewDataChannelDescription = "New date from web";
    public final static int NEW_SCHEDULE_PENDING_INTENT_ID = 423;
    public final static int NEW_EXAM_PENDING_INTENT_ID = 563;
    public final static int NEW_NEWS_PENDING_INTENT_ID = 805;
    public final static int NEW_TUITION_PENDING_INTENT_ID = 543;
    public final static int NEW_SCORE_PENDING_INTENT_ID = 947;

    private Context context;
    private static NotificationHelper instance;

    private NotificationHelper() {
        this.context = MyApplication.getContext();
        createNewDataChannel();
    }

    public static NotificationHelper getInstance() {
        if (instance == null) {
            instance = new NotificationHelper();
        }
        return instance;
    }

    public NotificationManagerCompat getNotificationManager() {
        return NotificationManagerCompat.from(context);
    }

    public void createNewDataChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NEW_DATA_CHANNEL_ID, NewDataChannelDescription, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.enableLights(true);
            channel.enableVibration(true);
            getNotificationManager().createNotificationChannel(channel);
        }
    }

    public NotificationCompat.Builder getScheduleNotification(String title, String content) {
        Intent openAppIntent = new Intent(context, MainActivity.class);
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, NEW_SCHEDULE_PENDING_INTENT_ID, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new NotificationCompat.Builder(context, NEW_DATA_CHANNEL_ID)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle(title)
                .setContentText(content)
                .setColor(Color.GREEN)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{0, 300, 150, 300})
                .setContentIntent(pendingIntent);
    }

    public NotificationCompat.Builder getExamNotification(String title, String content) {
        Intent openAppIntent = new Intent(context, MainActivity.class);
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        openAppIntent.putExtra("TAG", ExamFragment.TAG);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, NEW_EXAM_PENDING_INTENT_ID, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new NotificationCompat.Builder(context, NEW_DATA_CHANNEL_ID)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle(title)
                .setContentText(content)
                .setColor(Color.GREEN)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{0, 300, 150, 300})
                .setContentIntent(pendingIntent);
    }

    public void getNewsNotification(News news) {
        new NewsNotification().execute(news);
    }

    private class NewsNotification extends AsyncTask<News, Void, Bitmap> {

        private News news;

        @Override
        protected Bitmap doInBackground(News... params) {
            InputStream in;
            news = params[0];
            try {
                URL url = new URL(news.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            Intent detailNewsIntent = new Intent(context, DetailNewsActivity.class);
            detailNewsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            detailNewsIntent.putExtra(NewsFragment.KEY_ITEM, news);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

            Intent parentIntent = new Intent(context, MainActivity.class);
            parentIntent.putExtra("TAG", NewsFragment.TAG);

            taskStackBuilder.addNextIntentWithParentStack(parentIntent)
                    .addNextIntent(detailNewsIntent);

//            taskStackBuilder.addNextIntentWithParentStack(detailNewsIntent);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(NEW_NEWS_PENDING_INTENT_ID, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NEW_DATA_CHANNEL_ID)
                    .setSound(alarmSound)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.icon_app)
                    .setContentTitle(news.title)
                    .setContentText(news.summary)
                    .setLargeIcon(bitmap)
                    .setColor(Color.GREEN)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setVibrate(new long[]{0, 300, 150, 300})
                    .setContentIntent(pendingIntent);

            getNotificationManager().notify(AutoUpdateReceiver.NEW_NEWS_NOTIFICATION_ID, builder.build());
        }
    }

    public NotificationCompat.Builder getChildActivityNotification(int pendingIntentId, String title, String content, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(pendingIntentId, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new NotificationCompat.Builder(context, NEW_DATA_CHANNEL_ID)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle(title)
                .setContentText(content)
                .setColor(Color.GREEN)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{0, 300, 150, 300})
                .setContentIntent(pendingIntent);
    }

    public NotificationCompat.Builder getCountNotification(){
        int count = SharedPreferencesHelper.getInstance().getCountUpdateTime();
        count++;
        SharedPreferencesHelper.getInstance().setCountUpdateTime(count);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new NotificationCompat.Builder(context, NEW_DATA_CHANNEL_ID)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle("Số lần cập nhập: " + count)
                .setColor(Color.GREEN)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{0, 300, 150, 300});
    }
}
