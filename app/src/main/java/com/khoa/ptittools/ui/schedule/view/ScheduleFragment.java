package com.khoa.ptittools.ui.schedule.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.khoa.ptittools.R;
import com.khoa.ptittools.base.dialog.MyDialog;
import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.net.OnDownloadScheduleSuccess;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.databinding.FragmentScheduleBinding;
import com.khoa.ptittools.ui.schedule.util.ParseResponseSchedule;
import com.khoa.ptittools.ui.schedule.view.dialog.DownloadScheduleFragmentDialogListener;
import com.khoa.ptittools.ui.schedule.view.dialog.SelectWeekDialog;
import com.khoa.ptittools.ui.schedule.viewmodel.ScheduleViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ScheduleFragment extends Fragment {

    public static final String TAG = "schedule_fragment";
    private ScheduleViewModel mViewModel;
    private FragmentScheduleBinding mBinding;
    private CompositeDisposable disposable;
    private boolean isLoadWithSpinnerIndex = false;

    public ScheduleFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentScheduleBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);
        if (savedInstanceState == null) mViewModel.init(getActivity());

        disposable = new CompositeDisposable();
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

    private void setupBindings() {
        mViewModel.semester.observe(getViewLifecycleOwner(), new Observer<Semester>() {
            @Override
            public void onChanged(Semester semester) {
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

        mViewModel.semesterList.observe(getViewLifecycleOwner(), new Observer<List<Semester>>() {
            @Override
            public void onChanged(List<Semester> semesters) {
                if (!semesters.isEmpty()) {
                    List<String> semesterNameList = new ArrayList<>();
                    for (Semester semester : semesters) semesterNameList.add(semester.semesterName);
                    mBinding.spinnerSemester.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_drowdown, semesterNameList));
                } else {
                    showDialogDowloadScheduleFromWeb();
                }
            }
        });

        mBinding.txtRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDowloadScheduleFromWeb();
            }
        });

        mBinding.spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                User user = AppRepository.getInstance().takeUser();
                String currentSemesterCode = AppRepository.getInstance().getCurrentSemesterCode(user.maSV);
                int index = -1;
                for(int x=0; x< mViewModel.semesterList.getValue().size(); x++){
                    Semester semester = mViewModel.semesterList.getValue().get(x);
                    if(semester.semesterCode.equals(currentSemesterCode)){
                        index = x;
                        break;
                    }
                }
                if(!isLoadWithSpinnerIndex && index != i) {
                    mBinding.spinnerSemester.setSelection(index);
                    isLoadWithSpinnerIndex = true;
                } else {
                    String semesterCode = mViewModel.semesterList.getValue().get(i).semesterCode;
                    if(!isLoadWithSpinnerIndex) isLoadWithSpinnerIndex = true;
                    onSelectesSemester(semesterCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadSemester() {
        disposable.add(mViewModel.getSemesterListObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Semester>>() {
                    @Override
                    public void onSuccess(List<Semester> semesters) {
                        mViewModel.semesterList.postValue(semesters);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showMessage("Lỗi", e.getMessage());
                    }
                }));
    }

    private void showDialogDowloadScheduleFromWeb() {
        new DownloadScheduleFragmentDialogListener(new OnDownloadScheduleSuccess() {
            @Override
            public void onSuccess(Semester semester) {
                isLoadWithSpinnerIndex = false;
                loadSemester();
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

    private void onSelectesSemester(String semesterCode){
        User user = AppRepository.getInstance().takeUser();
        AppRepository.getInstance().setCurrentSemester(user.maSV, semesterCode);

        disposable.add(mViewModel.getLoadSemesterFromDbObservable(semesterCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Semester>() {
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
                        showMessage("Lỗi", e.getMessage());
                    }
                }));
    }

    private void showMessage(final String title, final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MyDialog(getActivity()).showNotificationDialog(title, message);
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
