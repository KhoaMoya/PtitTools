package com.khoa.ptittools.base.util;

import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.helper.FirebaseDatabaseHelper;
import com.khoa.ptittools.base.model.Exam;
import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.model.SemesterScore;
import com.khoa.ptittools.base.model.Subject;
import com.khoa.ptittools.base.model.Tuition;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.model.Week;
import com.khoa.ptittools.base.repository.AppRepository;

import java.util.ArrayList;
import java.util.List;

public class CompareUtil {

    public static boolean compareSchedule(Semester newSemester) {
        AppRepository appRepository = MyApplication.getAppRepository();
        if (newSemester == null) return false;

        User user = appRepository.takeUser();
        Semester oldSemester = appRepository.getSemester(user.maSV, appRepository.getCurrentSemesterCode(user.maSV));

        if (oldSemester == null) return true;
        if (!newSemester.semesterCode.equals(oldSemester.semesterCode)) return true;

        oldSemester.weekList = appRepository.getWeekInSemester(oldSemester.id);
        if (newSemester.weekList.size() != oldSemester.weekList.size()) return true;

        for (int i = 0; i < newSemester.weekList.size(); i++) {
            Week oldWeek = oldSemester.weekList.get(i);
            oldWeek.subjectList = appRepository.getSubjectInWeek(oldWeek.id);

            for (Subject subject : oldWeek.subjectList) subject.id = 0;

            if (!equalsList(oldWeek.subjectList, newSemester.weekList.get(i).subjectList)) {
                String str1 = ConvertersUtil.fromObject(oldWeek);
                String str2 = ConvertersUtil.fromObject(newSemester.weekList.get(i));
                FirebaseDatabaseHelper.saveDiffirentData(FirebaseDatabaseHelper.Schedule_Type, str1, str2);
                return true;
            }
        }

        return false;
    }

    public static News compareNewsList(List<News> newsList) {
        if (newsList == null || newsList.isEmpty()) return null;

        AppRepository appRepository = MyApplication.getAppRepository();
        List<News> oldNewsList = appRepository.getAllNews();
        if (oldNewsList == null || oldNewsList.isEmpty()) {
            return newsList.get(0);
        }

        if (newsList.size() != oldNewsList.size()) return newsList.get(0);

        for (int i = 0; i < newsList.size(); i++) {
            oldNewsList.get(i).updateTime = newsList.get(i).updateTime;
        }

        if (!equalsList(oldNewsList, newsList)) {
            String str1 = ConvertersUtil.fromList(oldNewsList);
            String str2 = ConvertersUtil.fromList(newsList);

            FirebaseDatabaseHelper.saveDiffirentData(FirebaseDatabaseHelper.News_Type, str1, str2);
            return newsList.get(0);
        }

        return null;
    }

    public static boolean compareTuition(Tuition newTuition) {
        if (newTuition == null) return false;

        AppRepository appRepository = MyApplication.getAppRepository();
        Tuition oldTuition = appRepository.getTuition(appRepository.takeUser().maSV);
        if (oldTuition == null) return true;

        oldTuition.lastUpdate = newTuition.lastUpdate;

        if (!equalsList(oldTuition.subjectList, newTuition.subjectList)) {
            String str1 = ConvertersUtil.fromObject(oldTuition);
            String str2 = ConvertersUtil.fromObject(newTuition);

            FirebaseDatabaseHelper.saveDiffirentData(FirebaseDatabaseHelper.Tuition_Type, str1, str2);
            return true;
        }

        return false;
    }

    public static boolean compareExam(List<Exam> newExamList) {
        if (newExamList == null) return false;

        AppRepository appRepository = MyApplication.getAppRepository();
        List<Exam> oldExamList = appRepository.getExamList(appRepository.takeUser().maSV);
        if (oldExamList == null) return true;

        if (newExamList.size() != oldExamList.size()) return true;

        for (int i = 0; i < newExamList.size(); i++) {
            Exam oldExam = oldExamList.get(i);
            Exam newExam = newExamList.get(i);

            oldExam.lastUpdate = newExam.lastUpdate;

            String str1 = ConvertersUtil.fromObject(oldExam);
            String str2 = ConvertersUtil.fromObject(newExam);
            if (!str1.equals(str2)) {
                FirebaseDatabaseHelper.saveDiffirentData(FirebaseDatabaseHelper.Exam_Type, str1, str2);
                return true;
            }
        }
        return false;
    }

    public static boolean compareScoreList(List<SemesterScore> newScoreList) {
        if (newScoreList == null) return false;
        AppRepository appRepository = MyApplication.getAppRepository();
        List<SemesterScore> oldScoreList = appRepository.getSemesterScore(appRepository.takeUser().maSV);
        if (oldScoreList == null || oldScoreList.isEmpty()) return true;

        if (oldScoreList.size() != newScoreList.size()) return true;

        for (int i = 0; i < newScoreList.size(); i++) {
            SemesterScore oldSemesterScore = oldScoreList.get(i);
            SemesterScore newSemesterScore = newScoreList.get(i);

            oldSemesterScore.id = 0;

            if (!equalsList(oldSemesterScore.subjectScoreList, newSemesterScore.subjectScoreList)) {
                String str1 = ConvertersUtil.fromList(oldSemesterScore.subjectScoreList);
                String str2 = ConvertersUtil.fromList(newSemesterScore.subjectScoreList);
                FirebaseDatabaseHelper.saveDiffirentData(FirebaseDatabaseHelper.Score_Type, str1, str2);
                return true;
            }
        }
        return false;
    }

    public static <T> boolean equalsList(List<T> list1, List<T> list2){
        if(list1.size()!=list2.size()) return false;

        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();

        for(int i=0; i<list1.size(); i++){
            list3.add(ConvertersUtil.fromObject(list1.get(i)));
            list4.add(ConvertersUtil.fromObject(list2.get(i)));
        }

        return list3.containsAll(list4);
    }

}
