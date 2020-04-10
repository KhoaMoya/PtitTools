package com.khoa.ptittools.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.helper.PermissionHelper;
import com.khoa.ptittools.base.helper.SharedPreferencesHelper;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        autoUpdate();
    }

    private void autoUpdate() {
        SharedPreferencesHelper preferencesHelper = MyApplication.getSharedPreferencesHelper();
        if (preferencesHelper.isFirstTime()) {
            preferencesHelper.setFirstTime(false);
            MyApplication.getSettingHelper().runAutoUpdate();
        }
    }

    private void checkPermissions(){
        PermissionHelper.checkPermissionBoot(this);
    }
}
