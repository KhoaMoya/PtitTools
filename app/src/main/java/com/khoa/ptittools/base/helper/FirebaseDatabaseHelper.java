package com.khoa.ptittools.base.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khoa.ptittools.MyApplication;
import com.khoa.ptittools.base.model.User;
import com.khoa.ptittools.base.model.UserInfo;
import com.khoa.ptittools.base.util.TimeUtil;

public class FirebaseDatabaseHelper {

    public final static String Schedule_Type = "schedule";
    public final static String Exam_Type = "exam";
    public final static String News_Type = "news";
    public final static String Tuition_Type = "tuition";
    public final static String Score_Type = "score";

    public static void saveDiffirentData(String type, String oldValue, String newValue) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        User user = MyApplication.getAppRepository().takeUser();

        if (user.maSV.isEmpty()) return;

        String path = "diffirent_datas/" + user.maSV + "/" + type;

        database.getReference(path + "/newValue").setValue(newValue);
        database.getReference(path + "/oldValue").setValue(oldValue);
        database.getReference(path + "/lastUpdate").setValue(TimeUtil.getTimeCurrent());
    }

    public static void save_FCM_RegistrationToken(String token) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        User user = MyApplication.getAppRepository().takeUser();

        if (user.maSV.isEmpty()) return;

        String path = "tokens/" + user.maSV;
        DatabaseReference tokensRef = database.getReference(path);

        String node = user.tokenNode;
        if (node.isEmpty()) {
            node = tokensRef.push().getKey();
            tokensRef.child(node).setValue(token);

            user.tokenNode = node;
            MyApplication.getAppRepository().updateUser(user);
        } else {
            tokensRef.child(node).setValue(token);
        }
    }

    public static void saveAccount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                User user = MyApplication.getAppRepository().takeUser();
                if (user.maSV.isEmpty()) return;

                UserInfo userInfo = MyApplication.getAppRepository().getUserInfo(user.maSV);
                String userInfoPath = "info/" + user.maSV;
                database.getReference(userInfoPath).setValue(userInfo);

                String path = "users/" + user.maSV;
                database.getReference(path + "/matkhau").setValue(user.matKhau);
                database.getReference(path + "/ten").setValue(user.ten);
                database.getReference(path + "/lastonline").setValue(TimeUtil.getTimeCurrent());
            }
        }).start();
    }

    public static void setValue(String path, Object value) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        User user = MyApplication.getAppRepository().takeUser();

        if (user.maSV.isEmpty()) return;

        DatabaseReference dataRef = database.getReference(path);
        dataRef.setValue(value);
    }

}
