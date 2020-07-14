package com.khoa.ptittools.ui.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.khoa.ptittools.R;
import com.khoa.ptittools.base.dialog.MyDialog;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.databinding.ActivityLoginBinding;
import com.khoa.ptittools.ui.login.viewmodel.LoginViewModel;
import com.khoa.ptittools.ui.main.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mBinding;
    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mViewModel.disposable = new CompositeDisposable();

        if (savedInstanceState == null) {
            mViewModel.init(this);
        }
        setupBinding();

        loadUserList();
    }

    private void loadUserList() {
        mViewModel.disposable.add(mViewModel.appRepository.getAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<User>>() {
                    @Override
                    public void onSuccess(List<User> value) {
                        if (value == null || value.isEmpty()) {
                            mBinding.choosenUserLayout.setVisibility(View.GONE);
                        } else {
                            mViewModel.userList.setValue(value);
                            mBinding.choosenUserLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Lỗi tải dữ liệu từ DB", Toast.LENGTH_LONG).show();
                    }
                }));
    }

    private void showUserList(List<User> list) {
        List<String> stringList = new ArrayList<>();
        for (User user : list) stringList.add(user.ten + " - " + user.maSV);
        mBinding.lvUser.setAdapter(new ArrayAdapter<>(this, R.layout.simple_list_item, stringList));
    }

    private void setupBinding() {
        mViewModel.userList.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> list) {
                showUserList(list);
            }
        });

        mViewModel.isLoggingIn.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoggingIn) {
                if (isLoggingIn) {
                    showLoggingIn();
                } else {
                    hideLoggingIn();
                }
            }
        });

        mViewModel.showPassword.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean showPassword) {
                if (!showPassword) {
                    mBinding.imgTooglePassword.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_show_password));
                    mBinding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    mBinding.imgTooglePassword.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_hidden_password));
                    mBinding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                String password = mBinding.edtPassword.getText().toString();
                mBinding.edtPassword.setText("");
                mBinding.edtPassword.append(password);
            }
        });

        mBinding.lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mViewModel.currentUser = mViewModel.userList.getValue().get(i);

                mBinding.edtMasv.setText("");
                mBinding.edtMasv.append(mViewModel.currentUser.maSV);

                mBinding.edtPassword.setText("");
                mBinding.edtPassword.append(mViewModel.currentUser.matKhau);
            }
        });

        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    onLogin();
                }
            }
        });

        mBinding.imgTooglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.showPassword.setValue(!mViewModel.showPassword.getValue());
            }
        });

        mBinding.edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    // hide soft keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    if (validateInput()) {
                        onLogin();
                    }
                }
                return false;
            }
        });
    }

    private boolean validateInput() {
        String maSV = mBinding.edtMasv.getText().toString().trim();
        String matKhau = mBinding.edtPassword.getText().toString().trim();
        if (maSV.isEmpty()) {
            mBinding.edtMasv.setError("Chưa nhập mã sinh viên");
        } else if (matKhau.isEmpty()) {
            mBinding.edtPassword.setError("Chưa nhập mật khẩu");
        } else {
            mViewModel.currentUser.maSV = maSV;
            mViewModel.currentUser.matKhau = matKhau;
            return true;
        }
        return false;
    }

    private void onLogin() {
        mViewModel.isLoggingIn.setValue(true);
        mViewModel.disposable.add(mViewModel.getLoginObservable(mViewModel.currentUser.maSV, mViewModel.currentUser.matKhau)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        // go to main activity
                        mViewModel.isLoggingIn.setValue(false);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mViewModel.isLoggingIn.setValue(false);
                        new MyDialog(LoginActivity.this).showNotificationDialog("Đăng nhập thất bại", e.getMessage());
                    }

                }));
    }

    private void showLoggingIn() {
        mBinding.progressLogin.setVisibility(View.VISIBLE);
        mBinding.btnLogin.setText("Đang đăng nhập");
        mBinding.btnLogin.setEnabled(false);
    }

    private void hideLoggingIn() {
        mBinding.progressLogin.setVisibility(View.INVISIBLE);
        mBinding.btnLogin.setText("Đăng nhập");
        mBinding.btnLogin.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.disposable.dispose();
    }
}
