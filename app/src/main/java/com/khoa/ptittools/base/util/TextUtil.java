package com.khoa.ptittools.base.util;

public class TextUtil {

    public static String convertToMoneyFormat(String text) {
        if (text == null) return "";
        text = text.replaceAll(" ", "");
        text = text.replaceAll("[^0-9]", "");

        String result = "";
        int c = 0;
        for (int i = text.length() - 1; i >= 0; i--) {
            c++;
            if (c == 4) {
                result += ".";
                c = 1;
            }
            result += text.charAt(i);
        }

        return new StringBuffer(result).reverse().toString();
    }
}
