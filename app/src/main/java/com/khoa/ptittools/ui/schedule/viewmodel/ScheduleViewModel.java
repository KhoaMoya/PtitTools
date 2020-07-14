package com.khoa.ptittools.ui.schedule.viewmodel;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.model.Subject;
import com.khoa.ptittools.base.model.Week;
import com.khoa.ptittools.base.repository.AppRepository;
import com.khoa.ptittools.ui.schedule.adapter.WeekViewPagerAdapter;
import com.khoa.ptittools.ui.schedule.util.ParseResponseSchedule;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class ScheduleViewModel extends ViewModel {

    private Context context;
    public AppRepository appRepository;

    public MutableLiveData<Semester> semester;
    public MutableLiveData<List<Semester>> semesterList;

    public WeekViewPagerAdapter adapter;
    public MutableLiveData<Integer> currentWeekIndex;

    public void init(Context context) {
        this.context = context;
        appRepository = MyApplication.getAppRepository();
        semester = new MutableLiveData<>();
        currentWeekIndex = new MutableLiveData<>();
        semesterList = new MutableLiveData<>();
    }

    private Semester loadSemesterFromDb(String userId, String semesterCode) {
        Semester semester = appRepository.getSemester(userId, semesterCode);
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

    public Single<Semester> getLoadSemesterFromDbObservable(final String semesterCode) {
        final String userId = appRepository.takeUser().maSV;
        return Single.create(new SingleOnSubscribe<Semester>() {
            @Override
            public void subscribe(SingleEmitter<Semester> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(loadSemesterFromDb(userId, semesterCode));
                }
            }
        });
    }

    public Single<List<Semester>> getSemesterListObservable(){
        final String userId = appRepository.takeUser().maSV;
        return Single.create(new SingleOnSubscribe<List<Semester>>() {
            @Override
            public void subscribe(SingleEmitter<List<Semester>> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(AppRepository.getInstance().getAllSemesterOfUser(userId));
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
