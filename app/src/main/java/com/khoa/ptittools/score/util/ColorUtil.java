package com.khoa.ptittools.score.util;

import android.content.Context;

import com.khoa.ptittools.R;

import java.util.ArrayList;
import java.util.List;

public class ColorUtil {

    public static List<Integer> getColorList(Context context) {
        List<Integer> colorList = new ArrayList<>();
        int[] colors = context.getResources().getIntArray(R.array.colors);

        for (int i = 0; i < colors.length; i++) {
            colorList.add(colors[i]);
        }
        return colorList;
    }

}
