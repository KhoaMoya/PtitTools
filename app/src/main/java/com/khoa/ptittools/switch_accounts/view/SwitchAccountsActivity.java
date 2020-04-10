package com.khoa.ptittools.switch_accounts.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.R;
import com.khoa.ptittools.base.dialog.MyDialog;
import com.khoa.ptittools.base.helper.SharedPreferencesHelper;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.databinding.ActivitySwitchAccountsBinding;
import com.khoa.ptittools.login.view.LoginActivity;
import com.khoa.ptittools.main.view.MainActivity;
import com.khoa.ptittools.switch_accounts.adapter.AccountOnClickListener;
import com.khoa.ptittools.switch_accounts.adapter.AccountRecyclerViewAdapter;
import com.khoa.ptittools.switch_accounts.dialog.DeleteAccountDialog;
import com.khoa.ptittools.switch_accounts.dialog.OnDeleteAccountListener;
import com.khoa.ptittools.switch_accounts.viewmodel.SwitchAccountsViewModel;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SwitchAccountsActivity extends AppCompatActivity implements AccountOnClickListener {

    private ActivitySwitchAccountsBinding mBinding;
    private SwitchAccountsViewModel mViewModel;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySwitchAccountsBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(SwitchAccountsViewModel.class);
        if(savedInstanceState == null) mViewModel.init();
        setContentView(mBinding.getRoot());

        setupBinding();
        loadAccounts();
    }

    private void setupBinding(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mViewModel.userList.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> list) {
                mViewModel.adapter = new AccountRecyclerViewAdapter(list);
                mViewModel.adapter.setAccountClickListener(SwitchAccountsActivity.this);
                mBinding.accountRv.setAdapter(mViewModel.adapter);
            }
        });

        mBinding.txtAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SwitchAccountsActivity.this, LoginActivity.class));
            }
        });
    }

    private void loadAccounts(){
        MyApplication.getAppRepository().getAllUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<User> list) {
                        if(list!=null && !list.isEmpty()){
                            mViewModel.userList.postValue(list);
                        } else {
                            Toast.makeText(SwitchAccountsActivity.this, "Không có tài khoản nào", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        new MyDialog(SwitchAccountsActivity.this).showNotificationDialog("Lỗi", e.getMessage());
                    }
                });
    }

    @Override
    public void onClickAccount(User user) {
        SharedPreferencesHelper.getInstance().saveUser(user);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClickDeleteAccount(User user) {
        new DeleteAccountDialog(this).showDeleteAccountDialog(user, new OnDeleteAccountListener() {
            @Override
            public void onDeleteAccount(final User user) {
                final ProgressDialog progressDialog = new ProgressDialog(SwitchAccountsActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Đang xóa dữ liệu");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyApplication.getAppRepository().deleteAccount(user);
                            progressDialog.dismiss();

                            loadAccounts();
                        }catch (Exception e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            new MyDialog(SwitchAccountsActivity.this).showNotificationDialog("Lỗi", e.getMessage());
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.none, R.anim.slide_right);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
        disposable.dispose();
    }
}
