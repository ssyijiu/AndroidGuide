package com.ssyijiu.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.format;

/**
 * Created by ssyijiu on 2016/9/29.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class DateUtil {

    /**
     * yyyy/MM/dd HH:mm           2016/09/29 12:34
     * yyyy-MM-dd HH:mm:ss        2016-09-29 12:34:20
     * yyyy-MM-dd HH:mm:ss EEEE   2016-09-29 12:34:20 星期四
     */
    private static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
        Locale.getDefault());


    private DateUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("DateUtil cannot be instantiated !");
    }


    public static String getCurrentTime() {
        return milliseconds2String(System.currentTimeMillis());
    }


    public static String getCurrentTime(Date date) {
        return milliseconds2String(System.currentTimeMillis());
    }


    public static String getCurrentTime(SimpleDateFormat format) {
        return milliseconds2String(System.currentTimeMillis(), format);
    }


    public static String date2String(Date date) {
        return date2String(date, DEFAULT_SDF);
    }


    /**
     * Date 换为字符串, format 为空, 使用默认格式: {@link DateUtil#DEFAULT_SDF}
     *
     * @param date 时间
     * @param format 转换格式
     * @return 时间字符串
     */
    public static String date2String(Date date, SimpleDateFormat format) {
        return format.format(date);
    }




    public static String milliseconds2String(long milliseconds) {

        return milliseconds2String(milliseconds, DEFAULT_SDF);
    }

    /**
     * 时间戳装换为字符串, format 为空, 使用默认格式: {@link DateUtil#DEFAULT_SDF}
     *
     * @param milliseconds 时间戳
     * @param format 转换格式
     * @return 时间字符串
     */
    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }


    public static long string2Milliseconds(String time) {
        return string2Milliseconds(time, DEFAULT_SDF);
    }


    /**
     * 字符串准换为时间戳, format 为空, 使用默认格式: {@link DateUtil#DEFAULT_SDF}
     *
     * @param time 时间字符串
     * @param format 转换格式
     * @return 时间戳, 出现异常返回 -1
     */
    public static long string2Milliseconds(String time, SimpleDateFormat format) {
        Date date;
        try {
            date = format.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
