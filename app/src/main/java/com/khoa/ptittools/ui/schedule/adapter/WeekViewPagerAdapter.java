package com.khoa.ptittools.ui.schedule.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.khoa.ptittools.base.model.Week;
import com.khoa.ptittools.ui.schedule.view.SchedulePageFragment;

import java.util.List;

public class WeekViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Week> weekList;

    public WeekViewPagerAdapter(@NonNull FragmentManager fm, List<Week> weekList) {
        super(fm);
        this.weekList = weekList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return SchedulePageFragment.newIntance(weekList.get(position));
    }

    @Override
    public int getCount() {
        return weekList == null ? 0 : weekList.size();
    }
}
