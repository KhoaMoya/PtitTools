package com.khoa.ptittools.ui.exam.view.dialog;

import android.content.Context;

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
