package com.khoa.ptittools.base.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.R;
import com.khoa.ptittools.base.model.User;

public class SharedPreferencesHelper {

    private static final String SharedPreferences_File = "com.khoa.ptittools_preferences";
    private SharedPreferences sharedPreferences;
    private static SharedPreferencesHelper instance;

    public SharedPreferencesHelper() {
        this.sharedPreferences = MyApplication.getContext().getSharedPreferences(SharedPreferences_File, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesHelper getInstance(){
        if(instance == null) {
            instance = new SharedPreferencesHelper();
        }
        return instance;
    }

    public synchronized void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ma_sv", user.maSV);
        editor.putString("mat_khau", user.matKhau);
        editor.putString("cookie", user.cookie);
        editor.putString("view_state", user.viewState);
        editor.apply();
    }

    public synchronized User takeUser() {
        String maSV = sharedPreferences.getString("ma_sv", "");
        String matKhau = sharedPreferences.getString("mat_khau", "");
        String cookie = sharedPreferences.getString("cookie", "");
        String viewState = sharedPreferences.getString("view_state", "");
        return new User(maSV, matKhau, cookie, viewState);
    }

    public void setFirstTime(boolean firstTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_time", firstTime);
        editor.apply();
    }

    public boolean isFirstTime() {
        return sharedPreferences.getBoolean("first_time", true);
    }

    public void setPeriodicValue(String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MyApplication.getContext().getString(R.string.periodic_update), value);
        editor.apply();
    }

    public long getPeriodicMilis() {
        int value = Integer.parseInt(sharedPreferences.getString(MyApplication.getContext().getString(R.string.periodic_update), "2"));
//        long time = 5 * 60 * 1000;
        long time = value * 60 * 60 * 1000;
        return time;
    }

    public boolean ishowNotification(){
        return sharedPreferences.getBoolean(MyApplication.getContext().getString(R.string.show_notification), true);
    }

    public synchronized void updateCookieAndViewState(String[] values) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!values[0].isEmpty()) editor.putString("cookie", values[0]);
        if (!values[1].isEmpty()) editor.putString("view_state", values[1]);
        editor.apply();
    }

    public void setCountUpdateTime(int count){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count_update_time", count);
        editor.apply();
    }

    public int getCountUpdateTime(){
        return sharedPreferences.getInt("count_update_time", 0);
    }
}
