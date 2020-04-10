package com.khoa.ptittools.schedule.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.model.Subject;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.model.Week;
import com.khoa.ptittools.base.net.Downloader;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.base.util.ParseResponse;
import com.khoa.ptittools.base.util.TimeUtil;
import com.khoa.ptittools.schedule.adapter.WeekViewPagerAdapter;
import com.khoa.ptittools.schedule.util.ParseResponseSchedule;

import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;

public class ScheduleViewModel extends ViewModel {

    private Context context;
    public AppRepository appRepository;

    public MutableLiveData<Semester> semester;

    public WeekViewPagerAdapter adapter;
    public MutableLiveData<Integer> currentWeekIndex;

    public void init(Context context) {
        this.context = context;
        appRepository = MyApplication.getAppRepository();
        semester = new MutableLiveData<>();
        currentWeekIndex = new MutableLiveData<>();
    }

    private Semester loadSemesterFromDb(String userId) {
        Semester semester = appRepository.getSemester(userId);
        if (semester == null) return new Semester();

        List<Week> weekList = appRepository.getWeekInSemester(semester.id);
        if (weekList == null) return new Semester();

        for (Week week : weekList) {
            List<Subject> subjectList = appRepository.getSubjectInWeek(week.id);
            week.subjectList = subjectList;
        }
        semester.weekList = weekList;

        return semester;
    }

    public Single<Semester> getLoadSemesterFromDbObservable() {
        final String userId = appRepository.takeUser().maSV;
        return Single.create(new SingleOnSubscribe<Semester>() {
            @Override
            public void subscribe(SingleEmitter<Semester> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(loadSemesterFromDb(userId));
                }
            }
        });
    }

    public WeekViewPagerAdapter getWeekAdapter(FragmentManager fragmentManager, List<Week> weekList){
        return new WeekViewPagerAdapter(fragmentManager, weekList);
    }

    public void backToStart(){
        if(semester.getValue()!=null && semester.getValue().weekList!=null && semester.getValue().weekList.size()>0) {
            currentWeekIndex.setValue(ParseResponseSchedule.getCurrentWeekIndex(semester.getValue().weekList));
        }
    }
}
