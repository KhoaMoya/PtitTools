package com.khoa.ptittools.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static SimpleDateFormat getTimeFormat(){
        return new SimpleDateFormat("HH:mm dd/MM/yyy");
    }

    public static String getTimeCurrent(){
        return getTimeFormat().format(new Date());
    }
}
