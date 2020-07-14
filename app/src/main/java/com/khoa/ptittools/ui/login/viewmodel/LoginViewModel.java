package com.khoa.ptittools.ui.login.viewmodel;

import android.content.Context;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.model.UserInfo;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.ui.profile.util.ParseResponseProfile;

import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

public class LoginViewModel extends ViewModel {

    private final String PROFILE_URL = "http://qldt.ptit.edu.vn/Default.aspx?page=xemdiemthi";
    public Context context;
    public CompositeDisposable disposable;
    public AppRepository appRepository;
    public MutableLiveData<List<User>> userList;
    public User currentUser;
    public MutableLiveData<Boolean> isLoggingIn;
    public MutableLiveData<Boolean> showPassword;

    public void init(Context context) {
        this.context = context;
        this.appRepository = MyApplication.getAppRepository();
        userList = new MutableLiveData<List<User>>(new ArrayList<User>());
        currentUser = new User();
        isLoggingIn = new MutableLiveData<>(false);
        showPassword = new MutableLiveData<>(false);
    }

    private void login(String maSv, String matKhau) throws Exception {
        // nếu user có trong userList
        for (User user : userList.getValue()) {
            if (user.maSV.equalsIgnoreCase(maSv) && user.matKhau.equals(matKhau)) {
                UserInfo userInfo = appRepository.getUserInfo(maSv);
                if (userInfo == null) {
                    userInfo = downloadProfileFromWeb();
                    appRepository.insertUserInfo(userInfo);
                }
                appRepository.saveUser(user);
                return;
            }
        }

        // nếu là tài khoản mới
        User user = Downloader.login(new User(maSv, matKhau, ""));
        appRepository.saveUser(user);

        UserInfo userInfo = appRepository.getUserInfo(maSv);
        if (userInfo == null) {
            userInfo = downloadProfileFromWeb();
            appRepository.insertUserInfo(userInfo);
        }
        user.ten = userInfo.ten;
        appRepository.insertUser(user);
        appRepository.saveUser(user);
    }

    private UserInfo downloadProfileFromWeb() throws Exception {
        Connection.Response response = Downloader.getHtml(PROFILE_URL);
        return ParseResponseProfile.convertToThongTin(response);
    }

    public Completable getLoginObservable(final String maSv, final String matKhau) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                if(!emitter.isDisposed()){
                    login(maSv, matKhau);
                    emitter.onComplete();
                }
            }
        });
    }
}
