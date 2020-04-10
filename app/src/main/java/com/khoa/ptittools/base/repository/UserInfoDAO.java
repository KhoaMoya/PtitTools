package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khoa.ptittools.base.model.UserInfo;

@Dao
public interface UserInfoDAO {

    @Query("Select * from userinfo where maSv = :maSv")
    UserInfo getUserInfo(String maSv);

    @Insert
    void insertUserInfo(UserInfo userInfo);

    @Update
    void updateUserInfo(UserInfo userInfo);

    @Delete
    void deleteUserInfo(UserInfo userInfo);
}
