package com.khoa.ptittools.exam.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.Exam;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.repository.AppRepository;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class ExamViewModel extends ViewModel {

    private Context context;
    private AppRepository appRepository;
    public MutableLiveData<List<Exam>> examList;


    public void init(Context context){
        this.context = context;
        this.appRepository = MyApplication.getAppRepository();
        examList = new MutableLiveData<>();
    }

    public Single<List<Exam>> getLoadExamListObservable(){
        return Single.create(new SingleOnSubscribe<List<Exam>>() {
            @Override
            public void subscribe(SingleEmitter<List<Exam>> emitter) throws Exception {
                if(!emitter.isDisposed()) {
                    User user = appRepository.takeUser();
                    emitter.onSuccess(appRepository.getExamList(user.maSV));
                }
            }
        });
    }

    public Single<List<Exam>> getDownloadExamListObservable(){
        return Single.create(new SingleOnSubscribe<List<Exam>>() {
            @Override
            public void subscribe(SingleEmitter<List<Exam>> emitter) throws Exception {
                if(!emitter.isDisposed()){
                    List<Exam> list = Downloader.downloadExamList();
                    appRepository.deleteOldExam(appRepository.takeUser().maSV);
                    appRepository.insertExamList(list);
                    emitter.onSuccess(list);
                }
            }
        });
    }
}
