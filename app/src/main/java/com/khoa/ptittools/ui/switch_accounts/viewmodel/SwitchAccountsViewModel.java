package com.khoa.ptittools.ui.switch_accounts.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.ui.switch_accounts.adapter.AccountRecyclerViewAdapter;

import java.util.List;

public class SwitchAccountsViewModel extends ViewModel {

    public MutableLiveData<List<User>> userList;
    public AccountRecyclerViewAdapter adapter;

    public void init() {
        userList = new MutableLiveData<>();
    }
}
