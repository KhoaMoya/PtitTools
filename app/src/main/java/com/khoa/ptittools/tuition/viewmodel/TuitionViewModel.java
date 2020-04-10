package com.khoa.ptittools.tuition.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.Tuition;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.repository.AppRepository;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

public class TuitionViewModel extends ViewModel {

    public MutableLiveData<Tuition> tuition;
    public Context context;
    public AppRepository appRepository;
    public Disposable disposable;

    public void init(Context context) {
        tuition = new MutableLiveData<>();
        this.context = context;
        appRepository = MyApplication.getAppRepository();
    }

    private Tuition loadTuition() throws Exception {
        User user = appRepository.takeUser();
        Tuition tuition = appRepository.getTuition(user.maSV);
        if(tuition == null) tuition = new Tuition();
        return tuition;
    }

    public Single<Tuition> getDownloadTuitionFromWebObservable(){
        return Single.create(new SingleOnSubscribe<Tuition>() {
            @Override
            public void subscribe(SingleEmitter<Tuition> emitter) throws Exception {
                if(!emitter.isDisposed()){
                    Tuition tuition = Downloader.downloadTuition();
                    appRepository.updateTuition(tuition);
                    emitter.onSuccess(tuition);
                }
            }
        });
    }

    public Single<Tuition> getLoadTuitionObservable() {
        return Single.create(new SingleOnSubscribe<Tuition>() {
            @Override
            public void subscribe(SingleEmitter<Tuition> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(loadTuition());
                }
            }
        });
    }

}
