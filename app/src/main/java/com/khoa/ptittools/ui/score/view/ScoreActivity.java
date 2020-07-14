package com.khoa.ptittools.ui.score.view;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.khoa.ptittools.R;
import com.khoa.ptittools.base.model.SemesterScore;
import com.khoa.ptittools.base.model.SubjectScore;
import com.khoa.ptittools.databinding.ActivityScoreBinding;
import com.khoa.ptittools.databinding.LabelItemBarChartBinding;
import com.khoa.ptittools.ui.score.viewmodel.ScoreViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class ScoreActivity extends AppCompatActivity {

    private ScoreViewModel mViewModel;
    private ActivityScoreBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        if (savedInstanceState == null) mViewModel.init(this);

        setupBinding();

        setupLineChart();

        loadSemesterScore();
    }

    private void loadSemesterScore() {
        mBinding.progressLoading.setVisibility(View.VISIBLE);
        mViewModel.getLoadSemesterScoreFromDbObservable()
                .subscribe(new SingleObserver<List<SemesterScore>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mViewModel.disposable = d;
                    }

                    @Override
                    public void onSuccess(List<SemesterScore> semesterScores) {
                        if (semesterScores == null || semesterScores.isEmpty()) {
                            downloadSemesterScoreFromWeb();
                        } else {
                            mViewModel.listSemesterScore.postValue(semesterScores);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mBinding.progressLoading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Tải dữ liệu từ DB thất bại", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void downloadSemesterScoreFromWeb() {
        mBinding.progressLoading.setVisibility(View.VISIBLE);
        mViewModel.getDownloadSemesterScoreFromWebObservable()
                .subscribe(new SingleObserver<List<SemesterScore>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mViewModel.disposable = d;
                    }

                    @Override
                    public void onSuccess(List<SemesterScore> semesterScores) {
                        mViewModel.listSemesterScore.postValue(semesterScores);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mBinding.progressLoading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Tải dữ liệu từ Web thất bại", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setupBinding() {
        mBinding.actionBar.txtTitle.setText("Xem Điểm");
        mBinding.rvSemesterScore.setAdapter(mViewModel.adapterRv);
        mBinding.rvSemesterScore.setNestedScrollingEnabled(false);

        mBinding.actionBar.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBinding.actionBar.txtRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadSemesterScoreFromWeb();
            }
        });

        mBinding.sumaryScore.layoutHocLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubjectScoreDialog(ScoreActivity.this, mViewModel.listHocLai,
                        "Các môn đã học lại"
                        , ContextCompat.getColor(ScoreActivity.this, R.color.colorBlack)).show();
            }
        });

        mBinding.sumaryScore.layoutHocCaiThien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubjectScoreDialog(ScoreActivity.this, mViewModel.listHocCaiThien,
                        "Các môn đã học cải thiện"
                        , ContextCompat.getColor(ScoreActivity.this, R.color.colorRed)).show();
            }
        });

        mViewModel.listSemesterScore.observe(this, new Observer<List<SemesterScore>>() {
            @Override
            public void onChanged(List<SemesterScore> semesterScores) {
                mBinding.progressLoading.setVisibility(View.GONE);
                showScore(semesterScores);
            }
        });

    }

    private void showScore(final List<SemesterScore> scoreList) {
        if (scoreList.size() <= 1) return;

        mViewModel.adapterRv.setParentListItem(scoreList);

        SemesterScore lastSemester = scoreList.get(scoreList.size() - 2);

        mBinding.txtHeadDtbtl.setText(lastSemester.diemTBTichLuy4);
        mBinding.sumaryScore.txtTinChiTichLuy.setText(lastSemester.soTinChiTichLuy);
        mBinding.sumaryScore.txtDaHocCaiThien.setText(mViewModel.listHocCaiThien.size() + "");
        mBinding.sumaryScore.txtDaHocLai.setText(mViewModel.listHocLai.size() + "");
        mBinding.sumaryScore.txtMonDaHoc.setText(mViewModel.getSoMonDaHoc());
        mBinding.sumaryScore.txtTinChiNo.setText(mViewModel.getSoTinChiNo());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                showLineChart(scoreList);
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    showTotalBarChart();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void showDiemTbTichLuy(final float score) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, score);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mBinding.txtHeadDtbtl.setText(String.format("%.2f", value));
            }
        });
        valueAnimator.setDuration((long) (score * 500));
        valueAnimator.start();
    }

    private void showTotalBarChart() {
        if (mViewModel.treeMapScore == null || mViewModel.treeMapScore.isEmpty()) return;

        mBinding.totalBarChart.containerItem.removeAllViews();
        mBinding.totalBarChart.layoutContainerLabel.removeAllViews();
        int total = 0;
        List<String> keyList = new ArrayList<>();
        List<List<SubjectScore>> subjectList = new ArrayList<>();

        // add elements to lists
        for (String key : mViewModel.treeMapScore.keySet()) {
            keyList.add(key);
            subjectList.add(mViewModel.treeMapScore.get(key));
        }

        // sort key lists
        for (int i = 0; i < keyList.size() - 1; i++) {
            for (int j = i + 1; j < keyList.size(); j++) {
                if (keyList.get(j).equals(keyList.get(i) + "+")) {
                    String tempKey = keyList.get(i);
                    keyList.set(i, keyList.get(j));
                    keyList.set(j, tempKey);

                    List<SubjectScore> tempList = subjectList.get(i);
                    subjectList.set(i, subjectList.get(j));
                    subjectList.set(j, tempList);

                    break;
                }
            }

            total += subjectList.get(i).size();
        }

        // draw bar chart
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            if (i != 0) {
                mBinding.totalBarChart.containerItem.addView(createItem("", 0.03f, false));
            }

            int size = subjectList.get(i).size();
            float weight = (float) size / (float) total * 10;

            View item = createItem(key, weight, true);
            mBinding.totalBarChart.containerItem.addView(item);

            View itemLabel = createLabelItemBarChart(subjectList.get(i).size(), key);
            mBinding.totalBarChart.layoutContainerLabel.addView(itemLabel);
        }

    }

    private View createLabelItemBarChart(int amount, final String key) {
        final int color = mViewModel.getColor(key);
        LabelItemBarChartBinding label = LabelItemBarChartBinding.inflate(getLayoutInflater(), mBinding.totalBarChart.layoutContainerLabel, false);

        label.imgColor.setColorFilter(color);
        label.txtLabel.setText(amount + key);

        label.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubjectScoreDialog(ScoreActivity.this, mViewModel.treeMapScore.get(key), "Các môn điểm " + key, color).show();
            }
        });

        return label.getRoot();
    }

    private View createItem(final String key, float weight, boolean clickable) {
        View view = new View(this);
        final int color = mViewModel.getColor(key);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = weight;
        view.setLayoutParams(params);
        view.setBackgroundColor(color);

        if (clickable) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SubjectScoreDialog(ScoreActivity.this, mViewModel.treeMapScore.get(key)
                            , "Các môn điểm " + key, color).show();
                }
            });

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        view.setScaleX(0.9f);
                        view.setScaleY(0.9f);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        view.setScaleX(1f);
                        view.setScaleY(1f);
                    }
                    return false;
                }
            });
        }
        return view;
    }

    private void setupLineChart() {
        mBinding.lineChart.setViewPortOffsets(50, 20, 40, 60);
        mBinding.lineChart.setBackgroundColor(Color.TRANSPARENT);
        mBinding.lineChart.getDescription().setEnabled(false);

        mBinding.lineChart.setTouchEnabled(false);
        mBinding.lineChart.setDrawGridBackground(false);
        mBinding.lineChart.setMaxHighlightDistance(300);

        mBinding.lineChart.getXAxis().setEnabled(true);
        XAxis x = mBinding.lineChart.getXAxis();
        x.setTextColor(Color.BLACK);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setGridColor(ContextCompat.getColor(this, R.color.colorGray));
        x.setTextSize(7);

        YAxis y = mBinding.lineChart.getAxisLeft();
        y.setEnabled(true);
        y.setDrawGridLines(true);
        y.setTextSize(7);
        y.setGridColor(ContextCompat.getColor(this, R.color.colorGray));


        mBinding.lineChart.getAxisRight().setEnabled(false);
        mBinding.lineChart.getLegend().setEnabled(false);

        mBinding.lineChart.animateX(1500);

        mBinding.lineChart.setScaleYEnabled(false);
    }

    private void showLineChart(List<SemesterScore> semesterScoreList) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < semesterScoreList.size(); i++) {
            if (semesterScoreList.get(i).diemTB4 != null) {
                entries.add(new Entry(i, Float.valueOf(semesterScoreList.get(i).diemTB4)));
            }
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Điểm trung bình hệ 4");
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
//        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setLineWidth(2f);

        // set the filled area
//        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return mBinding.lineChart.getAxisLeft().getAxisMinimum();
            }
        });
        lineDataSet.setColor(getResources().getColor(R.color.colorBlue));
        lineDataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.background_gradient_fade_blue));
        lineDataSet.setDrawValues(true);

//        lineDataSet.setDrawFilled(true);
//        lineDataSet.setFillColor(getResources().getColor(R.color.colorPrimary));

        lineDataSet.setFillAlpha(10);
        lineDataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return mBinding.lineChart.getAxisLeft().getAxisMinimum();
            }
        });

        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setCircleColor(getResources().getColor(R.color.colorBlue));

        LineData data = new LineData(lineDataSet);
        data.setValueTextSize(9f);
        data.setDrawValues(true);

        // set label cho truc X
        final ArrayList<String> labels = new ArrayList<>();
        for (SemesterScore diemHocKy : semesterScoreList) {
            String string = diemHocKy.tenHocKy;
            // Học kỳ 3 - Năm học 2018-2019
            try {
                String label = string.charAt(7) + ":" + string.charAt(21) + string.charAt(22) + "-" + string.charAt(26) + string.charAt(27);
                labels.add(label);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mBinding.lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labels.get(Math.round(value) - 1);
            }
        });

        // set data
        mBinding.lineChart.setData(data);
        mBinding.lineChart.invalidate();
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
        mViewModel.disposable.dispose();
    }
}
