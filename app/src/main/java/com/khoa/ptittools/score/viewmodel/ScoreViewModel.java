package com.khoa.ptittools.score.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.SemesterScore;
import com.khoa.ptittools.base.model.SubjectScore;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.score.adapter.SemesterScoreRecycleViewAdapter;
import com.khoa.ptittools.score.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ScoreViewModel extends ViewModel {

    private Context context;
    private AppRepository appRepository;
    public Disposable disposable;
    public MutableLiveData<List<SemesterScore>> listSemesterScore;
    public TreeMap<String, SubjectScore> treeMapSubject;
    public TreeMap<String, List> treeMapScore;
    public SemesterScoreRecycleViewAdapter adapterRv;
    public List<SubjectScore> listHocLai;
    public List<SubjectScore> listHocCaiThien;
    public List<Integer> colorList;

    public void init(Context context) {
        this.context = context;
        appRepository = MyApplication.getAppRepository();
        listSemesterScore = new MutableLiveData<>();
        treeMapSubject = new TreeMap<>();
        treeMapScore = new TreeMap<>();
        listHocLai = new ArrayList<>();
        listHocCaiThien = new ArrayList<>();
        adapterRv = new SemesterScoreRecycleViewAdapter(context, new ArrayList<SemesterScore>());
        colorList = ColorUtil.getColorList(context);
    }

    private List<SemesterScore> loadSemesterScoreFromDb() throws Exception {
        User user = appRepository.takeUser();
        List<SemesterScore> list = appRepository.getSemesterScore(user.maSV);
        convertData(list);

        return list;
    }

    public Single<List<SemesterScore>> getLoadSemesterScoreFromDbObservable() {
        return Single.create(new SingleOnSubscribe<List<SemesterScore>>() {
            @Override
            public void subscribe(SingleEmitter<List<SemesterScore>> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(loadSemesterScoreFromDb());
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<SemesterScore> downloadSemesterScoreFromWeb() throws Exception {
        List<SemesterScore> list = Downloader.downloadAllScore();
        appRepository.updateScore(list);
        convertData(list);

        return list;
    }

    public Single<List<SemesterScore>> getDownloadSemesterScoreFromWebObservable() {
        return Single.create(new SingleOnSubscribe<List<SemesterScore>>() {
            @Override
            public void subscribe(SingleEmitter<List<SemesterScore>> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(downloadSemesterScoreFromWeb());
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void convertData(List<SemesterScore> scoreList) {
        convertToTreeMapSubject(scoreList);
        convertToTreeMapScore();
    }

    public void convertToTreeMapSubject(List<SemesterScore> scoreList) {
        listHocLai.clear();
        listHocCaiThien.clear();
        treeMapSubject = new TreeMap<>();
        for (SemesterScore diemHocKy : scoreList) {
            for (SubjectScore diemMonHoc : diemHocKy.subjectScoreList) {
                String tenMonHoc = diemMonHoc.tenMonHoc;
                if (treeMapSubject.containsKey(tenMonHoc)) {
                    SubjectScore monHoc = treeMapSubject.get(tenMonHoc);
                    if (monHoc.TK4.equals("F")) listHocLai.add(diemMonHoc);
                    else listHocCaiThien.add(diemMonHoc);

                    // lấy điểm cao hơn
//                    if (monHoc.TK4.compareTo(diemMonHoc.TK4) < 0) {
//                        treeMapSubject.put(tenMonHoc, diemMonHoc);
//                    }
                }
                treeMapSubject.put(tenMonHoc, diemMonHoc);
            }
        }
    }

    public TreeMap<String, List> convertToTreeMapScore() {
        treeMapScore.clear();
        for (SubjectScore diemMonHoc : treeMapSubject.values()) {
            String diemChu = diemMonHoc.TK4;
            if (diemChu.isEmpty()) continue;
            if (treeMapScore.containsKey(diemChu)) {
                List<SubjectScore> list = treeMapScore.get(diemChu);
                list.add(diemMonHoc);
            } else {
                List<SubjectScore> list = new ArrayList<>();
                list.add(diemMonHoc);
                treeMapScore.put(diemChu, list);
            }
        }
        return treeMapScore;
    }

    public String getSoMonDaHoc() {
        int soMonDaHoc = 0;
        for (String tenMonHoc : treeMapSubject.keySet()) {
            if (!treeMapSubject.get(tenMonHoc).TK4.isEmpty())
                soMonDaHoc++;
        }
        return soMonDaHoc + "";
    }

    public String getSoTinChiNo() {
        List<SubjectScore> list = treeMapScore.get("F");
        int soTinChiNo = 0;

        if (list != null) {
            for (SubjectScore monHoc : list) {
                soTinChiNo += Integer.valueOf(monHoc.soTinChi);
            }
        }
        return soTinChiNo + "";
    }

    public int getColor(String key) {
        switch (key) {
            case "A+":
                return colorList.get(0);
            case "A":
                return colorList.get(1);
            case "B+":
                return colorList.get(2);
            case "B":
                return colorList.get(3);
            case "C+":
                return colorList.get(4);
            case "C":
                return colorList.get(5);
            case "D+":
                return colorList.get(6);
            case "D":
                return colorList.get(7);
            case "F":
                return colorList.get(8);
        }
        return colorList.get(9);
    }
}
