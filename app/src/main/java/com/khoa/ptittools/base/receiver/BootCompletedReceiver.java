package com.khoa.ptittools.base.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.khoa.ptittools.MyApplication;

public class BootCompletedReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        MyApplication.getSettingHelper().runAutoUpdate();
    }
}
