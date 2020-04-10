package com.khoa.ptittools.base.repository;

import android.content.Context;
import android.util.Log;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.helper.SharedPreferencesHelper;
import com.khoa.ptittools.base.model.Exam;
import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.model.SemesterScore;
import com.khoa.ptittools.base.model.Subject;
import com.khoa.ptittools.base.model.Tuition;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.model.UserInfo;
import com.khoa.ptittools.base.model.Week;
import com.khoa.ptittools.base.util.ConvertersUtil;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class AppRepository extends SharedPreferencesHelper {


    private AppDatabase appDatabase;

    private static AppRepository instance;

    public static AppRepository getInstance() {
        if (instance == null) {
            instance = new AppRepository(MyApplication.getContext());
        }
        return instance;
    }

    private AppRepository(Context context) {
        super();
        appDatabase = AppDatabase.getInstance(context);
    }

    public void deleteOldSemester(final Semester semester) {
        if(semester!=null) {
            deleteSemester(semester);
            deleteAllWeekOfUser(semester.maSv);
            deleteSubjectNotPrivateOfUser(semester.maSv);
        }
    }

    public void saveNewSemester(Semester semester) {
        insertSemester(semester);
        for (Week week : semester.weekList) {
            insertWeek(week);
            for (Subject subject : week.subjectList) {
                insertSubject(subject);
            }
        }
    }

    public void insertNews(News news) {
        appDatabase.newsDAO().insertNews(news);
    }

    public void deleteNews(News news) {
        appDatabase.newsDAO().deleteNews(news);
    }

    public void updateNews(List<News> list) {
        appDatabase.newsDAO().deleteAllNews();
        for (News news : list) {
            appDatabase.newsDAO().insertNews(news);
        }
    }

    public List<News> getAllNews() {
        return appDatabase.newsDAO().getAllNews();
    }

    public void deleteAlltNews() {
        appDatabase.newsDAO().deleteAllNews();
    }

    public Maybe<User> getUser(String maSv) {
        return appDatabase.userDAO().getUser(maSv);
    }

    public Single<List<User>> getAllUser() {
        return appDatabase.userDAO().getAllUser();
    }

    public void insertUser(User user) {
        appDatabase.userDAO().insertUser(user);
    }

    public void insertSemester(Semester semester) {
        appDatabase.semesterDAO().insertSemester(semester);
    }

    public void deleteSemester(Semester semester) {
        appDatabase.semesterDAO().deleteSemester(semester);
    }

    public void updateSemester(Semester semester) {
        User user = takeUser();
        if(user.maSV.isEmpty()) return;

        Semester oldSemester = getSemester(user.maSV);
        if (oldSemester != null) deleteOldSemester(oldSemester);

        if (semester != null) saveNewSemester(semester);
    }

    public Semester getSemester(String userId) {
        return appDatabase.semesterDAO().getSemester(userId);
    }

    public void updateTuition(Tuition tuition) {
        User user = takeUser();
        if(user.maSV.isEmpty()) return;

        Tuition oldTuition = getTuition(user.maSV);
        if (oldTuition != null) deleteTuition(oldTuition);

        if (tuition != null) insertTuition(tuition);
    }

    public void updateScore(List<SemesterScore> list) {
        User user = takeUser();
        if(user.maSV.isEmpty()) return;
        // delete old score
        deleteAllScore(user.maSV);
        // insert new score
        for (SemesterScore score : list) insertSemesterScore(score);
    }

    public List<Week> getAllWeek() {
        return appDatabase.weekDAO().getAllWeek();
    }

    public List<Week> getWeekInSemester(String semesterId) {
        return appDatabase.weekDAO().getWeekInSemester(semesterId);
    }

    public void insertWeek(Week week) {
        appDatabase.weekDAO().insertWeek(week);
    }

    public Completable updateWeek(Week week) {
        return appDatabase.weekDAO().updateWeek(week);
    }

    public void deleteWeek(Week week) {
        appDatabase.weekDAO().deleteWeek(week);
    }

    public void deleteAllWeekOfUser(String maSv) {
        appDatabase.weekDAO().deleteAllWeekOfUser(maSv);
    }

    public List<Subject> getAllSubjects() {
        return appDatabase.subjectDAO().getAllSubjects();
    }

    public List<Subject> getPrivateSubject() {
        return appDatabase.subjectDAO().getPrivateSubject();
    }

    public List<Subject> getSubjectInWeek(String weekId) {
        return appDatabase.subjectDAO().getSubjectInWeek(weekId);
    }

    public void insertSubject(Subject subject) {
        appDatabase.subjectDAO().insertSubject(subject);
    }

    public void deleteSubjectNotPrivateOfUser(String maSv) {
        appDatabase.subjectDAO().deleteSubjectNotPrivateOfUser(maSv);
    }

    public void deleteSubject(Subject subject) {
        appDatabase.subjectDAO().deleteSubject(subject);
    }

    public void updateSubject(Subject subject) {
        appDatabase.subjectDAO().updateSubject(subject);
    }

    public UserInfo getUserInfo(String maSv) {
        return appDatabase.userInfoDAO().getUserInfo(maSv);
    }

    public void insertUserInfo(UserInfo userInfo) {
        appDatabase.userInfoDAO().insertUserInfo(userInfo);
    }

    public void updateUserInfo(UserInfo userInfo) {
        appDatabase.userInfoDAO().updateUserInfo(userInfo);
    }

    public void deleteUserInfo(UserInfo userInfo) {
        if(userInfo!=null) appDatabase.userInfoDAO().deleteUserInfo(userInfo);
    }

    public Tuition getTuition(String maSv) {
        return appDatabase.tuitionDAO().getTuition(maSv);
    }

    public void insertTuition(Tuition tuition) {
        appDatabase.tuitionDAO().insertTuition(tuition);
    }

    public void deleteTuition(Tuition tuition) {
        if(tuition!=null) appDatabase.tuitionDAO().deleteTuition(tuition);
    }

    public List<SemesterScore> getSemesterScore(String maSv) {
        return appDatabase.scoreDAO().getSemesterScore(maSv);
    }

    public void insertSemesterScore(SemesterScore score) {
        appDatabase.scoreDAO().insertSemesterScore(score);
    }

    public void deleteSemesterScore(SemesterScore score) {
        appDatabase.scoreDAO().deleteSemesterScore(score);
    }

    public void deleteAllScore(String maSv) {
        appDatabase.scoreDAO().deleteAllScore(maSv);
    }

    public void deleteOldExam(String maSv) {
        appDatabase.examDAO().deleteOldExam(maSv);
    }

    public void insertExamList(List<Exam> list) {
        for (Exam exam : list) appDatabase.examDAO().insertExam(exam);
    }

    public List<Exam> getExamList(String maSv) {
        return appDatabase.examDAO().getExamList(maSv);
    }

    public void updateExamList(List<Exam> list){
        User user = takeUser();
        if(user.maSV.isEmpty()) return;

        deleteOldExam(user.maSV);
        insertExamList(list);
    }

    public void deleteUser(User user){
        appDatabase.userDAO().deleteUser(user);
    }

    public void deleteAccount(User user){
        deleteUserInfo(getUserInfo(user.maSV));
        deleteOldSemester(getSemester(user.maSV));
        deleteOldExam(user.maSV);
        deleteTuition(getTuition(user.maSV));
        deleteAllScore(user.maSV);
        deleteUser(user);
    }

}