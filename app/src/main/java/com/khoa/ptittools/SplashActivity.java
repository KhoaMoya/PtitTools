package com.khoa.ptittools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.ui.login.view.LoginActivity;
import com.khoa.ptittools.ui.main.view.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppRepository appRepository = MyApplication.getAppRepository();
                String maSV = appRepository.takeUser().maSV;
                if(maSV.isEmpty()){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 800);
    }
}
