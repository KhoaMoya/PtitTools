package com.khoa.ptittools.switch_accounts.adapter;

import com.khoa.ptittools.base.model.User;

public interface AccountOnClickListener {
    void onClickAccount(User user);
    void onClickDeleteAccount(User user);
}
