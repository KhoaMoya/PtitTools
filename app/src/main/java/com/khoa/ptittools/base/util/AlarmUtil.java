package com.khoa.ptittools.base.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.R;
import com.khoa.ptittools.base.receiver.AutoUpdateReceiver;

public class AlarmUtil {

    public static int AUTO_UPDATE_BROADCAST_REQUEST_CODE = 1234;

    public static void runAutoUpdate(long intervalMilis) {
        Context context = MyApplication.getContext();
        Intent intent = new Intent(context, AutoUpdateReceiver.class);
        intent.setAction(context.getString(R.string.auto_update));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AUTO_UPDATE_BROADCAST_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMilis, intervalMilis, pendingIntent);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, pendingIntent);
    }

    public static void cancelAutoUpdate(){
        Context context = MyApplication.getContext();
        AlarmManager alarmManager = (AlarmManager) MyApplication.getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AutoUpdateReceiver.class);
        intent.setAction(context.getString(R.string.auto_update));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AUTO_UPDATE_BROADCAST_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // huỷ báo thức của thiết lập trước
        alarmManager.cancel(pendingIntent);
    }
}
