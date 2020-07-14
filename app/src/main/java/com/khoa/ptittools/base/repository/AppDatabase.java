package com.khoa.ptittools.base.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.khoa.ptittools.base.model.Exam;
import com.khoa.ptittools.base.util.ConvertersSubjectScore;
import com.khoa.ptittools.base.util.ConvertersSubjectTuition;
import com.khoa.ptittools.base.model.News;
import com.khoa.ptittools.base.model.Semester;
import com.khoa.ptittools.base.model.SemesterScore;
import com.khoa.ptittools.base.model.Subject;
import com.khoa.ptittools.base.model.Tuition;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.model.UserInfo;
import com.khoa.ptittools.base.model.Week;


@Database(entities = {User.class
        , Semester.class
        , Week.class
        , Subject.class
        , UserInfo.class
        , News.class
        , Tuition.class
        , SemesterScore.class
        , Exam.class}, version = 2, exportSchema = false)
@TypeConverters({ConvertersSubjectTuition.class, ConvertersSubjectScore.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract SemesterDAO semesterDAO();
    public abstract WeekDAO weekDAO();
    public abstract SubjectDAO subjectDAO();
    public abstract UserInfoDAO userInfoDAO();
    public abstract NewsDAO newsDAO();
    public abstract TuitionDAO tuitionDAO();
    public abstract SemesterScoreDAO scoreDAO();
    public abstract ExamDAO examDAO();

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, "myptit_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }
}
