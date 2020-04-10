package com.khoa.ptittools.exam.view.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.khoa.ptittools.base.dialog.MyDialog;
import com.khoa.ptittools.databinding.ExamSettingDialogBinding;

public class ExamSettingDialog extends MyDialog {
    public ExamSettingDialog(Context context) {
        super(context);
    }

    public void showExamSettingDialog(){
        ExamSettingDialogBinding binding = ExamSettingDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        show();
    }

}
