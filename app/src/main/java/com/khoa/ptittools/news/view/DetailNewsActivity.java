package com.khoa.ptittools.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.khoa.ptittools.R;
import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.databinding.ActivityDetailNewsBinding;
import com.khoa.ptittools.news.util.ParseResponseNews;
import com.squareup.picasso.Picasso;

import org.jsoup.Connection;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailNewsActivity extends AppCompatActivity {

    private ActivityDetailNewsBinding mBinding;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityDetailNewsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        News news = (News) getIntent().getSerializableExtra(NewsFragment.KEY_ITEM);

        if (news != null) {
            mBinding.txtTitle.setText(news.title);
            mBinding.txtTime.setText(news.time);
            Picasso.get().load(news.imageUrl).centerCrop().resize(400, 240).into(mBinding.newsImg);
            downloadDetailNews(news.link);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e("Loi", "on new intent");
        super.onNewIntent(intent);
    }

    private void downloadDetailNews(final String link) {
        mBinding.progressLoading.setVisibility(View.VISIBLE);
        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {
                Connection.Response response = Downloader.getHtml(link, "");
                if(!emitter.isDisposed()) {
                    emitter.onSuccess(ParseResponseNews.convertNewsContent(response));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(String s) {
                        mBinding.progressLoading.setVisibility(View.GONE);
                        mBinding.webview.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mBinding.progressLoading.setVisibility(View.GONE);
                        Toast.makeText(DetailNewsActivity.this, "Tải dữ liệu thất bại", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void onClickBack(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getWindow().getSharedElementReturnTransition().setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getSharedElementEnterTransition().setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.none, R.anim.slide_right);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
        disposable.dispose();
    }
}
