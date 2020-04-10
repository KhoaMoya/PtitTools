package com.khoa.ptittools.profile.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.khoa.ptittools.base.dialog.MyDialog;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.model.UserInfo;
import com.khoa.ptittools.databinding.FragmentProfileBinding;
import com.khoa.ptittools.profile.viewmodel.ProfileViewModel;
import com.khoa.ptittools.score.view.ScoreActivity;
import com.khoa.ptittools.setting.SettingsActivity;
import com.khoa.ptittools.switch_accounts.view.SwitchAccountsActivity;
import com.khoa.ptittools.tuition.view.TuitionActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfileFragment extends Fragment {

    public static final String TAG = "profile_fragment";
    private FragmentProfileBinding mBinding;
    private ProfileViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        if (savedInstanceState == null) mViewModel.init(getContext());
        mViewModel.disposable = new CompositeDisposable();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupBinding();

        loadUserInfo();
    }

    private void loadUserInfo() {
        mBinding.progressLoading.setVisibility(View.VISIBLE);
        mViewModel.disposable.add(mViewModel.getLoadProfileObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        showUserInfo(userInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBinding.progressLoading.setVisibility(View.GONE);
                        new MyDialog(getActivity()).showNotificationDialog("Lỗi", e.getMessage());
                    }
                }));
    }

    private void showUserInfo(UserInfo userInfo){
        mBinding.progressLoading.setVisibility(View.GONE);

        mBinding.ten.setText(userInfo.ten);
        mBinding.masv.setText(userInfo.maSv);
        mBinding.noisinh.setText(userInfo.noisinh);
        mBinding.ngaysinh.setText(userInfo.ngaysinh);
        mBinding.lop.setText(userInfo.lop);
        mBinding.nganh.setText(userInfo.chuyennganh);
        mBinding.khoa.setText(userInfo.khoa);
        mBinding.hedaotao.setText(userInfo.hedaotao);
        mBinding.khoahoc.setText(userInfo.khoahoc);
        mBinding.covanhoctap.setText(userInfo.covanhoctap);
    }

    private void setupBinding(){
        mBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LogoutDialog(getActivity(), new LogoutDialog.OnLogout() {
                    @Override
                    public void onLogout() {
                        mViewModel.appRepository.saveUser(new User("", "", ""));
                    }
                }).show();
            }
        });

        mBinding.actionShowScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ScoreActivity.class));
            }
        });

        mBinding.actionShowTuition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TuitionActivity.class));
            }
        });

        mBinding.actionSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });

        mBinding.actionSwitchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SwitchAccountsActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.disposable.dispose();
    }
}