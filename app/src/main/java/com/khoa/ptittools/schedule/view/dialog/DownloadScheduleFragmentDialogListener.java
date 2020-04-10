package com.khoa.ptittools.schedule.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.R;
import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.net.OnDownloadScheduleSuccess;
import com.khoa.ptittools.base.net.PTIT_URL;
import com.khoa.ptittools.base.net.ScheduleDownloaderListener;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.databinding.DialogDownloadScheduleBinding;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DownloadScheduleFragmentDialogListener extends DialogFragment implements ScheduleDownloaderListener {

    enum STATUS {
        LOADING,
        ERROR,
        SUCCESS
    }

    private String Schedule_URL = PTIT_URL.Schedule_URL;
    private AppRepository appRepository;
    private DialogDownloadScheduleBinding binding;
    private Disposable disposable;
    private Single<Semester> downloadObservable;
    private MutableLiveData message;
    private MutableLiveData<Integer> progress;
    private MutableLiveData<STATUS> status;
    private boolean cancel;
    private OnDownloadScheduleSuccess listener;

    public DownloadScheduleFragmentDialogListener(OnDownloadScheduleSuccess listener) {
        this.listener = listener;
        appRepository = MyApplication.getAppRepository();
        message = new MutableLiveData<>();
        progress = new MutableLiveData<>();
        status = new MutableLiveData<>();

        Schedule_URL += appRepository.takeUser().maSV;
        downloadObservable = getDownloadSemesterFromWebObservable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogDownloadScheduleBinding.inflate(getLayoutInflater(), container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.MyDialog;
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupBinding();
        downloadSchedule();
    }

    private void setupBinding() {
        setCancelable(false);
        message.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.txtStatus.setText(s);
            }
        });

        progress.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.progress.setProgress(integer);
            }
        });

        status.observe(getViewLifecycleOwner(), new Observer<STATUS>() {
            @Override
            public void onChanged(STATUS status) {
                if (status == STATUS.LOADING) {
                    onUiLoading();
                } else if (status == STATUS.ERROR) {
                    onUiError();
                } else if (status == STATUS.SUCCESS) {
                    onUiSuccess();
                }
            }
        });

        binding.btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadSchedule();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel = true;
                disposable.dispose();
                dismiss();
            }
        });
    }

    private void downloadSchedule() {
        status.postValue(STATUS.LOADING);
        message.postValue("Đang tải dữ liệu");
        progress.postValue(0);
        downloadObservable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Semester>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Semester semester) {
                        status.postValue(STATUS.SUCCESS);
                        listener.onSuccess(semester);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        message.postValue("Lỗi. Cập nhật dữ liệu thất bại");
                        status.postValue(STATUS.ERROR);
                    }
                });
    }

    private Semester downloadScheduleFromWeb() throws Exception {
        Semester semester = Downloader.downloadSemester(this);

        if (!cancel) {
            message.postValue("Đang xóa dữ liệu cũ");
            Semester oldSemester = appRepository.getSemester(appRepository.takeUser().maSV);
            if (oldSemester != null) appRepository.deleteOldSemester(oldSemester);

            message.postValue("Đang thêm dữ liệu mới");
            appRepository.saveNewSemester(semester);
        }

        return semester;
    }

    private Single<Semester> getDownloadSemesterFromWebObservable() {
        return Single.create(new SingleOnSubscribe<Semester>() {
            @Override
            public void subscribe(SingleEmitter<Semester> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(downloadScheduleFromWeb());
                }
            }
        });
    }

    @Override
    public boolean isCanceled() {
        return cancel;
    }

    @Override
    public void onUpdateProgress(String mes, int pro) {
        message.postValue(mes);
        progress.postValue(pro);
    }

    private void onUiError() {
        binding.btnTryAgain.setVisibility(View.VISIBLE);
        binding.txtStatus.setTextColor(Color.RED);
    }

    private void onUiLoading() {
        binding.btnTryAgain.setVisibility(View.GONE);
        binding.txtStatus.setTextColor(Color.BLACK);
    }

    private void onUiSuccess() {
        binding.btnTryAgain.setVisibility(View.GONE);
        binding.txtStatus.setTextColor(Color.BLACK);
        binding.btnCancel.setVisibility(View.GONE);
        message.setValue("Cập nhật thành công");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
        binding = null;
    }
}
