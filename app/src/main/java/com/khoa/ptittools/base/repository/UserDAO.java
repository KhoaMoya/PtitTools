package com.khoa.ptittools.base.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khoa.ptittools.base.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user where masv= :maSv")
    Maybe<User> getUser(String maSv);

    @Query("SELECT * FROM user")
    Single<List<User>> getAllUser();

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Update
    void updateUser(User user);
}
