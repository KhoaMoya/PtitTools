package com.khoa.ptittools.ui.main.viewmodel;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.ui.main.bottomnavigation.BottomNavigationItemSelectListener;
import com.khoa.ptittools.ui.main.bottomnavigation.MyBackStack;
import com.khoa.ptittools.ui.main.bottomnavigation.MyBottomNavigation;
import com.khoa.ptittools.ui.main.bottomnavigation.MyItem;

import java.util.List;

public class MainViewModel extends ViewModel {
    public String currentTag;
    public Fragment currentFragment;
    public MyBackStack backStack;
    public MyBottomNavigation bottomNavigation;

    public void init() {
        currentTag = "";
        currentFragment = new Fragment();
        backStack = new MyBackStack();
    }

    public void setupBottomNavigation(ViewGroup container, List<MyItem> list, BottomNavigationItemSelectListener listener) {
        bottomNavigation = new MyBottomNavigation(container, list, listener);
    }
}
