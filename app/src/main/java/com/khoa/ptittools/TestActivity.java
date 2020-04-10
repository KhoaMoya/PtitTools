package com.khoa.ptittools;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.khoa.ptittools.base.model.MyException;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.base.util.ParseResponse;
import com.khoa.ptittools.databinding.ActivityTestBinding;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    ActivityTestBinding binding;
    MutableLiveData<String> body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        body = new MutableLiveData<>();

        body.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.webview.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
            }
        });

        binding.webview.setInitialScale(100);
        binding.webview.getSettings().setBuiltInZoomControls(true);
        binding.webview.getSettings().setDisplayZoomControls(false);
        binding.webview.getSettings().setJavaScriptEnabled(true);

        passCapcha();

    }

    private void passCapcha() {
        Single.create(new SingleOnSubscribe<Connection.Response>() {
            @Override
            public void subscribe(SingleEmitter<Connection.Response> emitter) throws Exception {
                emitter.onSuccess(Downloader.getHtml("http://qldt.ptit.edu.vn/Default.aspx?page=xemhocphi"));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Connection.Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Connection.Response response) {
                        body.postValue(response.body());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("Loi", "on error: " + e.getMessage());
                    }
                });

    }
}
