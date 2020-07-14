package com.khoa.ptittools.ui.schedule.view.dialog;

import android.content.Context;

import com.khoa.ptittools.base.dialog.MyDialog;
import com.khoa.ptittools.base.model.Subject;
import com.khoa.ptittools.databinding.DialogDetailSubjectBinding;

public class DetailSubjectDialog  extends MyDialog {

    public DetailSubjectDialog(Context context) {
        super(context);
    }

    public void showDetailSubject(Subject subject){
        setCancelable(true);
        DialogDetailSubjectBinding binding = DialogDetailSubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtSubjectName.setText(subject.subjectName);
        binding.txtGroup.setText(subject.subjectCode);
        binding.txtClass.setText(subject.classCode);
        binding.txtRoom.setText(subject.roomName);
        binding.txtTeacher.setText(subject.teacher);
        binding.txtTinchi.setText(subject.soTinChi);

        show();
    }

}
