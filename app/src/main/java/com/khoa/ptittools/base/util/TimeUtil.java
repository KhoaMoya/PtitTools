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

    public static String getDayInWeek(int index){
        switch (index){
            case 2: return "Hai";
            case 3: return "Ba";
            case 4: return "Tư";
            case 5: return "Năm";
            case 6: return "Sáu";
            case 7: return "Bảy";
            case 8: return "CN";
        }
        return "";
    }
}
