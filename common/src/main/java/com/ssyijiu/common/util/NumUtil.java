package com.ssyijiu.common.util;

import java.text.DecimalFormat;

/**
 * Created by ssyijiu on 2016/11/2.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class NumUtil {
    private NumUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("NumUtil cannot be instantiated !");
    }


    /**
     * String 转 int, 异常返回 0
     * <p/> 数据展示的时候使用防止 crash，数据传给后台的时候不应该使用
     *
     * @return the int value of numStr, if Exception return 0
     */
    public static int parseInt(String numStr) {
        int number = 0;
        try {
            number = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }


    /**
     * String 转 long, 异常返回 0
     * <p/> 数据展示的时候使用防止 crash，数据传给后台的时候不应该使用
     *
     * @return the long value of numStr, if Exception return 0
     */
    public static long parseLong(String numStr) {
        long number = 0;
        try {
            number = Long.parseLong(numStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }


    /**
     * String 转 double, 异常返回 0
     * <p/> 数据展示的时候使用防止 crash，数据传给后台的时候不应该使用
     *
     * @return the double value of numStr, if Exception return 0
     */
    public static double parseDouble(String numStr) {
        double number = 0;
        try {
            number = Double.parseDouble(numStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }


    /**
     * 将字符串格式化成带小数点形式
     *
     * @param numStr 字符串
     * @param len 小数位数
     * @return xxx.xxx
     */
    public static String decimalFormat(String numStr, int len) {
        double number = parseDouble(numStr);
        return decimalFormat(number, len);
    }


    /**
     * format double  ->  xxx.xxx
     */
    public static String decimalFormat(double number, int len) {

        if (number == 0) {
            return "0";
        }

        StringBuilder format = new StringBuilder("#.");

        if (len == 0) {
            format.deleteCharAt(1); // 删除小数点
        }

        for (int i = 0; i < len; i++) {
            format.append("0");
        }

        return new DecimalFormat(format.toString()).format(number);
    }


    public static String formatPercentage(String num) {

        return formatPercentage(parseDouble(num));
    }


    /**
     * if num < 1, transform it xx.xx%
     */
    public static String formatPercentage(double num) {

        if (num == 0) {
            return "0";
        }

        if (num < 1.0) {
            return new DecimalFormat("0.00%").format(num);
        }
        return String.valueOf(num);
    }
}
