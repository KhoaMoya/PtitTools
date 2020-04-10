package com.khoa.ptittools.profile.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.UserInfo;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.net.PTIT_URL;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.profile.util.ParseResponseProfile;

import org.jsoup.Connection;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;

public class ProfileViewModel extends ViewModel {

    private Context context;
    public AppRepository appRepository;
    public CompositeDisposable disposable;

    public void init(Context context) {
        this.context = context;
        appRepository = MyApplication.getAppRepository();
    }

    public UserInfo loadUserInfo() throws Exception {
        String maSV = appRepository.takeUser().maSV;
        UserInfo userInfo = appRepository.getUserInfo(maSV);
        if (userInfo == null) {
            userInfo = downloadProfileFromWeb();
            appRepository.insertUserInfo(userInfo);
        }
        return userInfo;
    }

    public Single<UserInfo> getLoadProfileObservable() {
        return Single.create(new SingleOnSubscribe<UserInfo>() {
            @Override
            public void subscribe(SingleEmitter<UserInfo> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(loadUserInfo());
                }
            }
        });
    }

    private UserInfo downloadProfileFromWeb() throws Exception {
        Connection.Response response = Downloader.getHtml(PTIT_URL.Profile_URL);
        return ParseResponseProfile.convertToThongTin(response);
    }
}