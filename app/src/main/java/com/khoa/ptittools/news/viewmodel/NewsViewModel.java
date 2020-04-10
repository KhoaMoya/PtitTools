package com.khoa.ptittools.news.viewmodel;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.net.PTIT_URL;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.news.adapter.NewsRecycleViewAdapter;
import com.khoa.ptittools.news.util.ParseResponseNews;

import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

public class NewsViewModel extends ViewModel {

    public MutableLiveData<List<News>> newsList;
    public NewsRecycleViewAdapter adapter;
    private Activity activity;
    public AppRepository appRepository;
    public Disposable disposable;

    public void init(Activity activity) {
        this.activity = activity;
        newsList = new MutableLiveData<>();
        adapter = new NewsRecycleViewAdapter(activity);
        appRepository = MyApplication.getAppRepository();
    }

    public Single<List<News>> downloadNewsListPageObservable(final int amountPage) {
        return Single.create(new SingleOnSubscribe<List<News>>() {
            @Override
            public void subscribe(SingleEmitter<List<News>> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    List<News> list = Downloader.downloadNewsList(amountPage);
                    appRepository.updateNews(list);
                    emitter.onSuccess(list);
                }
            }
        });
    }
}
