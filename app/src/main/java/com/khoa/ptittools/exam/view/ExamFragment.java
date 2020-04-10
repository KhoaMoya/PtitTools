package com.khoa.ptittools.exam.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.khoa.ptittools.base.dialog.MyDialog;
import com.khoa.ptittools.base.model.Exam;
import com.khoa.ptittools.databinding.FragmentExamBinding;
import com.khoa.ptittools.exam.adapter.ExamRecyclerViewAdapter;
import com.khoa.ptittools.exam.view.dialog.ExamSettingDialog;
import com.khoa.ptittools.exam.viewmodel.ExamViewModel;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ExamFragment extends Fragment {

    public final  static String TAG = "exam_fragment";

    private FragmentExamBinding mBinding;
    private ExamViewModel mViewModel;
    private Disposable disposable;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentExamBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ExamViewModel.class);
        if(savedInstanceState==null) mViewModel.init(getContext());

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupBinding();
        loadExams();
    }

    private void loadExams(){
        onUiLoading();
        mViewModel.getLoadExamListObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Exam>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<Exam> list) {
                        if(list == null || list.isEmpty()){
                            downloadExamList();
                        }else {
                            mViewModel.examList.postValue(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onUiShow();
                        new MyDialog(getActivity()).showNotificationDialog("Lỗi", e.getMessage());
                    }
                });
    }

    private void setupBinding(){
        mBinding.appbarLayout.fragTitle.setText("Lịch thi");
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mViewModel.examList.observe(getViewLifecycleOwner(), new Observer<List<Exam>>() {
            @Override
            public void onChanged(List<Exam> list) {
                if(!list.isEmpty()) mBinding.appbarLayout.txtLastUpdate.setText("Cập nhật lần cuối " + list.get(0).lastUpdate);
                mBinding.recyclerView.setAdapter(new ExamRecyclerViewAdapter(list));
                onUiShow();
            }
        });

        mBinding.appbarLayout.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadExamList();
            }
        });

        mBinding.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ExamSettingDialog(getActivity()).showExamSettingDialog();
            }
        });
    }

    private void downloadExamList(){
        onUiLoading();
        mViewModel.getDownloadExamListObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Exam>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<Exam> list) {
                        if(list!=null){
                            mViewModel.examList.postValue(list);
                        } else {
                            onUiShow();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onUiShow();
                        new MyDialog(getActivity()).showNotificationDialog("Lỗi", e.getMessage());
                    }
                });
    }

    private void onUiLoading(){
        mBinding.progressLoading.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.INVISIBLE);
    }

    private void onUiShow(){
        mBinding.progressLoading.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
        disposable.dispose();
    }
}
