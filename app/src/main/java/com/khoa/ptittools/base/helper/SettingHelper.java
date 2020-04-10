package com.khoa.ptittools.base.helper;

import com.khoa.ptittools.base.util.AlarmUtil;

public class SettingHelper {

    private SharedPreferencesHelper preferencesUtil;
    private static SettingHelper instance;

    private SettingHelper() {
        this.preferencesUtil = SharedPreferencesHelper.getInstance();
    }

    public static SettingHelper getInstance() {
        if (instance == null) {
            instance = new SettingHelper();
        }
        return instance;
    }

    public void runAutoUpdate() {
        long intervalMilis = preferencesUtil.getPeriodicMilis();
        AlarmUtil.cancelAutoUpdate();
        AlarmUtil.runAutoUpdate(intervalMilis);
    }

    public void cancelAutoUpdate(){
        AlarmUtil.cancelAutoUpdate();
    }
}
