package com.khoa.ptittools.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.helper.FirebaseDatabaseHelper;
import com.khoa.ptittools.base.helper.PermissionHelper;
import com.khoa.ptittools.base.model.User;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveOnlineInfo();
        getFirebaseIntanceId();
        checkPermissions();
        autoUpdate();

    }

    private void autoUpdate() {
        if (MyApplication.getAppRepository().isFirstTime()) {
            MyApplication.getSharedPreferencesHelper().setFirstTime(false);
            MyApplication.getSettingHelper().runAutoUpdate();
        }
    }

    private void checkPermissions() {
        PermissionHelper.checkPermissionBoot(this);
    }

    private void getFirebaseIntanceId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("Loi", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        updateToken(token);
                    }
                });
    }

    private void saveOnlineInfo() {
        FirebaseDatabaseHelper.saveAccount();
    }

    private void updateToken(final String token){
        final User user = MyApplication.getAppRepository().takeUser();
        if(!token.equals(user.token)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    user.token = token;
                    MyApplication.getAppRepository().updateUser(user);
                    MyApplication.getAppRepository().saveUser(user);

                    FirebaseDatabaseHelper.save_FCM_RegistrationToken(token);
                }
            }).start();
        }
    }
}
