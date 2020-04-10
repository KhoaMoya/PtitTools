package com.khoa.ptittools.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.databinding.FragmentNewsBinding;
import com.khoa.ptittools.news.adapter.ItemClickListener;
import com.khoa.ptittools.news.viewmodel.NewsViewModel;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsFragment extends Fragment implements ItemClickListener {

    public static final String TAG = "news_fragment";
    private NewsViewModel mViewModel;
    private FragmentNewsBinding mBinding;

    public final static String KEY_ITEM = "keyItem";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentNewsBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        if (mViewModel.newsList == null) mViewModel.init(getActivity());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupBinding();

        loadNewsList();
    }

    private void loadNewsList() {
        mBinding.progressLoading.setVisibility(View.VISIBLE);
        Single.create(new SingleOnSubscribe<List<News>>() {
            @Override
            public void subscribe(SingleEmitter<List<News>> emitter) throws Exception {
                emitter.onSuccess(mViewModel.appRepository.getAllNews());
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<News>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mViewModel.disposable = d;
                    }

                    @Override
                    public void onSuccess(List<News> newsList) {
                        if (newsList == null || newsList.isEmpty()) {
                            downloadNewsList();
                        } else {
                            mViewModel.newsList.postValue(newsList);
                            updateNewsList(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mBinding.progressLoading.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Lỗi tải dữ liệu từ DB", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void downloadNewsList() {
        mViewModel.downloadNewsListPageObservable(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<News>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mViewModel.disposable = d;
                    }

                    @Override
                    public void onSuccess(List<News> newsList) {
                        mViewModel.newsList.postValue(newsList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mBinding.progressLoading.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Lỗi tải dữ liệu từ Web", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setupBinding() {
        mBinding.appbarLayout.fragTitle.setText("Tin tức");
        mBinding.recyclerView.setAdapter(mViewModel.adapter);
        mViewModel.adapter.setItemClickListener(this);

        mViewModel.newsList.observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> newsList) {
                mBinding.progressLoading.setVisibility(View.GONE);
                mBinding.appbarLayout.txtLastUpdate.setText("Cập nhật lần cuối " + newsList.get(0).updateTime);
                mViewModel.adapter.setNewsList(newsList);
            }
        });
        mBinding.appbarLayout.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNewsList(false);
            }
        });
    }

    private void updateNewsList(boolean inBackground) {
        if (inBackground) {
            mBinding.progressLoading.setVisibility(View.GONE);
            downloadNewsList();
        } else {
            mBinding.progressLoading.setVisibility(View.VISIBLE);
            downloadNewsList();
        }
    }

//    @Override
//    public void onClickItem(int position, ActivityOptionsCompat optionsCompat) {
//        Intent intent = new Intent(getActivity(), DetailNewsActivity.class);
//        intent.putExtra(KEY_ITEM, mViewModel.newsList.getValue().get(position));
//        startActivity(intent, optionsCompat.toBundle());
//    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(getActivity(), DetailNewsActivity.class);
        intent.putExtra(KEY_ITEM, mViewModel.newsList.getValue().get(position));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
        mViewModel.disposable.dispose();
    }
}
