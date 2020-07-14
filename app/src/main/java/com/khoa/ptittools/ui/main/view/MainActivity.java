package com.khoa.ptittools.ui.main.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.khoa.ptittools.R;
import com.khoa.ptittools.databinding.ActivityMainBinding;
import com.khoa.ptittools.ui.exam.view.ExamFragment;
import com.khoa.ptittools.base.BaseActivity;
import com.khoa.ptittools.ui.main.bottomnavigation.BottomNavigationItemSelectListener;
import com.khoa.ptittools.ui.main.bottomnavigation.MyItem;
import com.khoa.ptittools.ui.main.viewmodel.MainViewModel;
import com.khoa.ptittools.ui.news.view.NewsFragment;
import com.khoa.ptittools.ui.profile.view.ProfileFragment;
import com.khoa.ptittools.ui.schedule.view.ScheduleFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BottomNavigationItemSelectListener {

    private ActivityMainBinding mBinding;
    private MainViewModel mMainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setupBottomNavigation();

        if (savedInstanceState == null) {
            mMainViewModel.init();
            showFragment();
        }
    }

    private void showFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            String fragmentTag = intent.getStringExtra("TAG");

            if (fragmentTag == null || fragmentTag.isEmpty()) {
                swapFragment(ScheduleFragment.TAG, true);
            } else {
                swapFragment(fragmentTag, true);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showFragment();
    }

    private void setupBottomNavigation() {
        mMainViewModel.setupBottomNavigation(mBinding.btmNav, createItemList(), this);
    }

    @Override
    public void onSelectedItemChanged(String tag) {
        swapFragment(tag, true);
    }

    public void swapFragment(String tag, boolean addToBackStack) {
        mMainViewModel.bottomNavigation.setItemSelected(tag);
        Fragment newFragment = null;
        switch (tag) {
            case NewsFragment.TAG:
                newFragment = getSupportFragmentManager().findFragmentByTag(NewsFragment.TAG);
                if (newFragment == null) newFragment = new NewsFragment();
                break;
            case ExamFragment.TAG:
                newFragment = getSupportFragmentManager().findFragmentByTag(ExamFragment.TAG);
                if (newFragment == null) newFragment = new ExamFragment();
                break;
            case ScheduleFragment.TAG:
                newFragment = getSupportFragmentManager().findFragmentByTag(ScheduleFragment.TAG);
                if (newFragment == null) newFragment = new ScheduleFragment();
                break;
            case ProfileFragment.TAG:
                newFragment = getSupportFragmentManager().findFragmentByTag(ProfileFragment.TAG);
                if (newFragment == null) newFragment = new ProfileFragment();
                break;
        }
        if (newFragment != null) {
            loadFragment(newFragment, tag, addToBackStack);
        }
    }

    public void loadFragment(Fragment newFragment, String tag, boolean addToBackStack) {
        if (mMainViewModel.currentTag.equals(tag) && tag.equals(ScheduleFragment.TAG)) {
            ScheduleFragment scheduleFragment = (ScheduleFragment) mMainViewModel.currentFragment;
            scheduleFragment.backToStart();
        } else if (mMainViewModel.currentTag.equals(tag)) {
            return;
        }

        if (mMainViewModel.currentTag.isEmpty()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout, newFragment, tag)
                    .commit();
        } else {
            Fragment prevfragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (prevfragment == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainViewModel.currentFragment)
                        .add(R.id.frame_layout, newFragment, tag)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainViewModel.currentFragment)
                        .show(newFragment)
                        .commit();
            }
        }

        mMainViewModel.currentFragment = newFragment;
        mMainViewModel.currentTag = tag;
        if (addToBackStack) mMainViewModel.backStack.push(tag);
    }

    private void backFragment() {
        String tag = mMainViewModel.backStack.pop();
        swapFragment(tag, false);
    }

    @Override
    public void onBackPressed() {
        FragmentManager childFragmentManager = mMainViewModel.currentFragment.getChildFragmentManager();
        if (childFragmentManager.getBackStackEntryCount() == 0) {
            if (mMainViewModel.backStack.isEnd()) {
                String fragmentTag = mMainViewModel.backStack.get(0);
                if(!fragmentTag.equals(ScheduleFragment.TAG)){
                    mMainViewModel.backStack.remove(0);
                    swapFragment(ScheduleFragment.TAG, true);
                }else {
                    finish();
                }
            } else {
                backFragment();
            }
        } else {
            childFragmentManager.popBackStack();
        }
    }

    private List<MyItem> createItemList() {
        List<MyItem> itemList = new ArrayList<>();
        itemList.add(new MyItem("Thời khóa biểu", ScheduleFragment.TAG, R.drawable.ic_schedule));
        itemList.add(new MyItem("Lịch thi", ExamFragment.TAG, R.drawable.ic_exam));
        itemList.add(new MyItem("Tin tức", NewsFragment.TAG, R.drawable.ic_news));
        itemList.add(new MyItem("Tài khoản", ProfileFragment.TAG, R.drawable.ic_profile));
        return itemList;
    }
}
