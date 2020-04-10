package com.khoa.ptittools.base.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khoa.ptittools.base.util.TimeUtil;

public class FirebaseDatabaseHelper {

    public final static String Schedule_Path = "schedule";
    public final static String Exam_Path = "exam";
    public final static String News_Path = "news";
    public final static String Tuition_Path = "tuition";
    public final static String Score_Path = "score";

    public static void writeData(String path, String oldValue, String newValue) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference newValueRef = database.getReference(path + "/newValue");
        newValueRef.setValue(newValue);

        DatabaseReference oldValueRef = database.getReference(path + "/oldValue");
        oldValueRef.setValue(oldValue);

        DatabaseReference lastUpdateRef = database.getReference(path + "/lastUpdate");
        lastUpdateRef.setValue(TimeUtil.getTimeCurrent());
    }
}
