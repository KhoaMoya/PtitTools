package com.khoa.ptittools.switch_accounts.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.switch_accounts.adapter.AccountRecyclerViewAdapter;

import java.util.List;

public class SwitchAccountsViewModel extends ViewModel {

    public MutableLiveData<List<User>> userList;
    public AccountRecyclerViewAdapter adapter;

    public void init() {
        userList = new MutableLiveData<>();
    }
}
