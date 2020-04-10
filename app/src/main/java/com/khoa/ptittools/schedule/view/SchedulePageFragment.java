package com.khoa.ptittools.schedule.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khoa.ptittools.base.model.Subject;
import com.khoa.ptittools.base.model.Week;
import com.khoa.ptittools.databinding.FragmentViewpageScheduleBinding;
import com.khoa.ptittools.schedule.view.dialog.DetailSubjectDialog;
import com.khoa.scheduleview.OnScheduleClickListener;
import com.khoa.scheduleview.SimpleSubject;

import java.util.List;

public class SchedulePageFragment extends Fragment {

    private Week mWeek;
    private FragmentViewpageScheduleBinding mBinding;

    public SchedulePageFragment() {
    }

    private SchedulePageFragment(Week week) {
        mWeek = week;
    }

    public static SchedulePageFragment newIntance(Week week) {
        return new SchedulePageFragment(week);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentViewpageScheduleBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mWeek = (Week) savedInstanceState.getSerializable("week_data");
        }

        if (mWeek != null) {
            showSchedule(mWeek.subjectList);
        }
    }

    private void showSchedule(final List<Subject> subjects) {
        SimpleSubject[] subArr = new SimpleSubject[subjects.size()];
        for (int i = 0; i < subjects.size(); i++) {
            Subject sub = subjects.get(i);
            subArr[i] = new SimpleSubject(sub.day, sub.startLesson, sub.durationLesson, sub.subjectName, sub.roomName);
        }

        mBinding.scheculeView.setSubjects(subArr);

        mBinding.scheculeView.setOnScheduleClickListener(new OnScheduleClickListener() {
            @Override
            public void onClickSubject(int subjectIndex) {
                new DetailSubjectDialog(getContext()).showDetailSubject(subjects.get(subjectIndex));
            }

            @Override
            public void onLongClickSubject(int subjectIndex) {
            }

            @Override
            public void onClickAddEvent(int day, int startLesson) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("week_data", mWeek);
    }
}