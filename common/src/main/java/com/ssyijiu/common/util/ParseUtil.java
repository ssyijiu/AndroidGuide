package com.ssyijiu.common.util;

import android.text.TextUtils;
import java.text.DecimalFormat;

/**
 * Created by ssyijiu on 2016/11/2.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class ParseUtil {
    private ParseUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("ParseUtil cannot be instantiated !");
    }


    /**
     * String 转 int, 异常返回 0
     *
     * @return the int value of numStr, if Exception return 0
     */
    public static int parseInt(String numStr) {
        int number = 0;
        if (!TextUtils.isEmpty(numStr)) {
            try {
                number = Integer.parseInt(numStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return number;
    }


    /**
     * String 转 long, 异常返回 0
     *
     * @return the long value of numStr, if Exception return 0
     */
    public static long parseLong(String numStr) {
        long number = 0;
        if (!TextUtils.isEmpty(numStr)) {
            try {
                number = Long.parseLong(numStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return number;
    }


    /**
     * String 转 double, 异常返回 0
     *
     * @return the double value of numStr, if Exception return 0
     */
    public static double parseDouble(String numStr) {
        double number = 0;
        if (!TextUtils.isEmpty(numStr)) {
            try {
                number = Double.parseDouble(numStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return number;
    }


    /**
     * String 转 float, 异常返回 0
     *
     * @return the float value of numStr, if Exception return 0
     */
    public static float parseFloat(String numStr) {
        float number = 0;
        if (!TextUtils.isEmpty(numStr)) {
            try {
                number = Float.parseFloat(numStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return number;
    }


    public static String decimalFormat(String numStr, int len) {
        double number = parseDouble(numStr);
        return decimalFormat(number, len);
    }


    /**
     * 将数字转换成带小数点形式
     *
     * @param number 要装换的数字
     * @param len 小数位数
     * @return xxx.xxx
     */
    public static String decimalFormat(double number, int len) {
        if (number == 0) {
            return "0";
        }
        StringBuilder sbr = new StringBuilder("#.");
        if (len == 0) {
            sbr.deleteCharAt(1); // 删除小数点
        }
        for (int i = 0; i < len; i++) {
            sbr.append("0");
        }
        return new DecimalFormat(sbr.toString()).format(number);
    }


    public static String percentFormat(String num) {
        return percentFormat(parseDouble(num));
    }


    /**
     * 如果数字小于 0 装换为百分比形式
     *
     * @param num 要转换的数字
     * @return xx%
     */
    public static String percentFormat(double num) {

        if (num == 0) {
            return "0";
        }
        if (num < 1.0) {
            return new DecimalFormat("0.00%").format(num);
        }
        return String.valueOf(num);
    }


    /**
     * if null -> ""
     */
    public static String parseNull(String str) {
        return parseNull(str, "");
    }


    /**
     * if null -> other
     */
    public static String parseNull(String str, String other) {
        if (TextUtils.isEmpty(str)
                || str.trim().equals("")
                || str.equalsIgnoreCase("null")
                || str.equalsIgnoreCase("(null)")) {
            return other;
        }
        return str;
    }
}
