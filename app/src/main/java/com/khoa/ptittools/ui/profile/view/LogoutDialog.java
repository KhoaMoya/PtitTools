package com.khoa.ptittools.ui.profile.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.khoa.ptittools.R;
import com.khoa.ptittools.ui.login.view.LoginActivity;

public class LogoutDialog extends Dialog {

    public LogoutDialog(@NonNull final Activity activity, final OnLogout listener) {
        super(activity);
        setContentView(R.layout.dialog_confirm);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogLogout;
        getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);

        TextView txtLogout = findViewById(R.id.txt_ok);
        TextView txtCancel = findViewById(R.id.txt_cancel);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (listener != null) {
                    listener.onLogout();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }, 300);

            }
        });
    }

    public interface OnLogout {
        void onLogout();
    }
}
