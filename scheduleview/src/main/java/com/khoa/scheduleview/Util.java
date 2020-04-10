package com.khoa.scheduleview;

import android.content.Context;

public class Util {
    public static float convertDpToPixel(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
