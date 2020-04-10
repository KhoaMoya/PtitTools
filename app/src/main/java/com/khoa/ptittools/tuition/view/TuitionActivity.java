package com.khoa.ptittools.tuition.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.khoa.ptittools.R;
import com.khoa.ptittools.base.model.SubjectTuition;
import com.khoa.ptittools.base.model.Tuition;
import com.khoa.ptittools.base.util.TextUtil;
import com.khoa.ptittools.databinding.ActivityTuitionBinding;
import com.khoa.ptittools.tuition.viewmodel.TuitionViewModel;

import java.util.ArrayList;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TuitionActivity extends AppCompatActivity {

    private ActivityTuitionBinding mBinding;
    private TuitionViewModel mViewModel;

    private ArrayList<View> listRow = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityTuitionBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(TuitionViewModel.class);
        if (savedInstanceState == null) mViewModel.init(this);

        setupBinding();

        loadTuition();
    }

    private void loadTuition() {
        onUiLoading();
        mViewModel.getLoadTuitionObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Tuition>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mViewModel.disposable = d;
                    }

                    @Override
                    public void onSuccess(Tuition tuition) {
                        if (tuition.subjectList.isEmpty()) {
                            downloadTuitionFromWeb();
                        } else {
                            mViewModel.tuition.postValue(tuition);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onUiFinnish();
                        Toast.makeText(getApplicationContext(), "Tải dữ liệu từ DB thất bại", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void downloadTuitionFromWeb() {
        onUiLoading();
        mViewModel.getDownloadTuitionFromWebObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Tuition>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mViewModel.disposable = d;
                    }

                    @Override
                    public void onSuccess(Tuition tuition) {
                        mViewModel.tuition.postValue(tuition);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onUiFinnish();
                        Toast.makeText(getApplicationContext(), "Tải dữ liệu từ Web thất bại", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setupBinding() {
        mBinding.actionBar.txtTitle.setText("Học Phí");
        mBinding.actionBar.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mBinding.actionBar.txtRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadTuitionFromWeb();
            }
        });

        mViewModel.tuition.observe(this, new Observer<Tuition>() {
            @Override
            public void onChanged(Tuition tuition) {
                showTuition(tuition);
            }
        });
    }

    private void onUiLoading() {
        mBinding.scrollview.setVisibility(View.INVISIBLE);
        mBinding.progressLoading.setVisibility(View.VISIBLE);
    }

    private void onUiFinnish() {
        mBinding.scrollview.setVisibility(View.VISIBLE);
        mBinding.progressLoading.setVisibility(View.GONE);
    }

    private void showTuition(Tuition tuition) {
        onUiFinnish();

        mBinding.txtHeadSoTienConNo.setText(TextUtil.convertToMoneyFormat(tuition.soTienConNo));
        mBinding.soTaiKhoan.setText("STK " + tuition.soTaiKhoan);
        mBinding.lastUpdate.setText(tuition.lastUpdate);

        mBinding.tongSoTinChi.setText(tuition.tongSoTinChi);
        mBinding.tongSoTinChiHocPhi.setText(tuition.tongSoTinChiHocPhi);
        mBinding.tongSoTienHocPhiHocKy.setText(tuition.tongSoTienHocPhiHocKy);
        mBinding.soTienDongToiThieuLanDau.setText(TextUtil.convertToMoneyFormat(tuition.soTienDongToiThieuLanDau));
        mBinding.soTienDaDongTrongHocKy.setText(TextUtil.convertToMoneyFormat(tuition.soTienDaDongTrongHocKy));
        mBinding.soTienConNo.setText(TextUtil.convertToMoneyFormat(tuition.soTienConNo));

        addSubjectList(tuition);

        startAnimation();
    }

    private void startAnimation(){
        mBinding.txtHeadSoTienConNo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < listRow.size(); i++) {
                    final View view = listRow.get(i);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setVisibility(View.VISIBLE);
                            view.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_down_fade_in));
                        }
                    }, 100 * i);
                }
            }
        }, 200);
    }

    private void addSubjectList(final Tuition tuition) {
        mBinding.container.removeAllViews();
        for (SubjectTuition subject : tuition.subjectList) {
            View row = LayoutInflater.from(TuitionActivity.this).inflate(R.layout.item_subject_tuition, mBinding.container, false);
            TextView txtSubjectName = row.findViewById(R.id.txt_subject_name);
            TextView txtSTC = row.findViewById(R.id.txt_stc);
            TextView txt_tuition = row.findViewById(R.id.txt_tuition);

            txtSubjectName.setText(subject.tenMonHoc);
            txtSTC.setText(subject.soTinChi);
            txt_tuition.setText(TextUtil.convertToMoneyFormat(subject.phaiDong));

            row.setVisibility(View.INVISIBLE);

            listRow.add(row);
            mBinding.container.addView(row);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void onClickCopy(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Số tài khoản", mViewModel.tuition.getValue().soTaiKhoan == null ? "" : mViewModel.tuition.getValue().soTaiKhoan);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Đã copy số tài khoản", Toast.LENGTH_SHORT).show();
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
        mViewModel.disposable.dispose();
    }
}
