package com.khoa.ptittools.schedule.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.net.OnDownloadScheduleSuccess;
import com.khoa.ptittools.databinding.FragmentScheduleBinding;
import com.khoa.ptittools.schedule.util.ParseResponseSchedule;
import com.khoa.ptittools.schedule.view.dialog.DownloadScheduleFragmentDialogListener;
import com.khoa.ptittools.schedule.view.dialog.SelectWeekDialog;
import com.khoa.ptittools.schedule.viewmodel.ScheduleViewModel;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ScheduleFragment extends Fragment {

    public static final String TAG = "schedule_fragment";
    private ScheduleViewModel mViewModel;
    private FragmentScheduleBinding mBinding;
    private Disposable disposable;

    public ScheduleFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentScheduleBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);
        if (savedInstanceState == null) mViewModel.init(getActivity());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupBindings();

        setupViewPagerWeek();

        setupClickTuan();

        if (mViewModel.semester.getValue() == null) {
            loadSemester();
        }
    }

    private void loadSemester() {
        mViewModel.getLoadSemesterFromDbObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Semester>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Semester semester) {
                        if (!semester.weekList.isEmpty()) {
                            mViewModel.semester.setValue(semester);
                        } else {
                            showDialogDowloadScheduleFromWeb();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showDialogDowloadScheduleFromWeb() {
        new DownloadScheduleFragmentDialogListener(new OnDownloadScheduleSuccess() {
            @Override
            public void onSuccess(Semester semester) {
                mViewModel.semester.postValue(semester);
            }
        }).show(getChildFragmentManager(), "");
    }

    private void setupClickTuan() {
        mBinding.selectTuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SelectWeekDialog(getActivity(), mViewModel.currentWeekIndex.getValue(), mViewModel.semester.getValue().weekList).showSelectWeekDialog(new SelectWeekDialog.OnCheckWeekRadioButtonListener() {
                    @Override
                    public void onWeekSelected(int index) {
                        mViewModel.currentWeekIndex.postValue(index);
                    }
                });
            }
        });

        mBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.viewpager.arrowScroll(View.FOCUS_RIGHT);
            }
        });
        mBinding.prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.viewpager.arrowScroll(View.FOCUS_LEFT);
            }
        });
    }

    private void setupViewPagerWeek() {
        mBinding.viewpager.setAdapter(mViewModel.adapter);
        mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewModel.currentWeekIndex.postValue(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupBindings() {
        mViewModel.semester.observe(getViewLifecycleOwner(), new Observer<Semester>() {
            @Override
            public void onChanged(Semester semester) {
                mBinding.txtSemester.setText(semester.semesterName);
                mBinding.txtLastUpdate.setText("Cập nhật lần cuối " + semester.lastUpdate);

                mBinding.viewpager.setAdapter(mViewModel.getWeekAdapter(getChildFragmentManager(), semester.weekList));
                mViewModel.currentWeekIndex.setValue(ParseResponseSchedule.getCurrentWeekIndex(semester.weekList));


            }
        });

        mViewModel.currentWeekIndex.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                mBinding.txtWeek.setText(mViewModel.semester.getValue().weekList.get(index).weekName);
                mBinding.txtWeekTime.setText(mViewModel.semester.getValue().weekList.get(index).startDate + " - "
                        + mViewModel.semester.getValue().weekList.get(index).endDate);
                if (mBinding.viewpager.getCurrentItem() != index) {
                    mBinding.viewpager.setCurrentItem(index);
                }
            }
        });

        mBinding.txtRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDowloadScheduleFromWeb();
            }
        });

    }

    public void backToStart() {
        mViewModel.backToStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
        disposable.dispose();
    }
}
