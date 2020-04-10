package com.khoa.ptittools.base.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.khoa.ptittools.base.dialog.MyDialog;

public class PermissionHelper {

    private static int MY_PERMISSIONS_REQUEST_RECEIVE_BOOT_COMPLETED = 4324;

    public static void checkPermissionBoot(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECEIVE_BOOT_COMPLETED);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, MY_PERMISSIONS_REQUEST_RECEIVE_BOOT_COMPLETED);
        }
    }
}
