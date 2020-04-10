package com.khoa.ptittools;

import android.app.Application;

import com.khoa.ptittools.base.helper.NotificationHelper;
import com.khoa.ptittools.base.helper.SettingHelper;
import com.khoa.ptittools.base.helper.SharedPreferencesHelper;
import com.khoa.ptittools.base.repository.AppRepository;

public class MyApplication extends Application {

    private static MyApplication context;

    public static MyApplication getContext(){
        return context;
    }

    public static AppRepository getAppRepository(){
        return AppRepository.getInstance();
    }

    public static SettingHelper getSettingHelper(){
        return SettingHelper.getInstance();
    }

    public static NotificationHelper getNotificationHelper(){
        return NotificationHelper.getInstance();
    }

    public static SharedPreferencesHelper getSharedPreferencesHelper(){
        return SharedPreferencesHelper.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
