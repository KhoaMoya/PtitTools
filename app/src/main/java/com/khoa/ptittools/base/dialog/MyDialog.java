package com.khoa.ptittools.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.R;
import com.khoa.ptittools.databinding.DialogNotificationBinding;

import java.util.Objects;

public class MyDialog extends Dialog{

    public MyDialog(Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.MyDialog;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void showNotificationDialog(String title, String content){
        setCancelable(true);
        DialogNotificationBinding binding = DialogNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtTitle.setText(title);
        binding.txtContent.setText(content);

        show();
    }
}
