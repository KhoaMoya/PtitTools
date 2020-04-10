package com.khoa.ptittools.switch_accounts.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.khoa.ptittools.base.dialog.MyDialog;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.databinding.DialogConfirmBinding;

public class DeleteAccountDialog extends MyDialog {

    public DeleteAccountDialog(Context context) {
        super(context);
    }

    public void showDeleteAccountDialog(final User user, final OnDeleteAccountListener listener){
        setCancelable(true);
        DialogConfirmBinding binding = DialogConfirmBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());

        binding.txtContent.setText("Xác nhận xóa tài khoản " + user.ten + " - " + user.maSV);
        binding.txtOk.setText("Xóa");

        binding.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.onDeleteAccount(user);
            }
        });

        show();
    }
}
