package com.ssyijiu.common.log;

import android.text.TextUtils;
import android.util.Log;
import java.util.Locale;

/**
 * Created by ssyijiu on 2016/8/16.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */
public class MLog {

    /** 日志的TAG */
    public static String TAG = "";

    /** 日志级别，默认为 V */
    private static LogLev MLev;


    private MLog() {
    /* cannot be instantiated */
        throw new UnsupportedOperationException("MLog cannot be instantiated !");
    }


    /**
     * 设置日志级别，只有高于设置级别的日志才打印。
     *
     * @param lev {@link LogLev#V,LogLev#D,LogLev#I,LogLev#W,LogLev#E,LogLev#NO_LOG}
     */
    public static void setLogLev(LogLev lev) {
        MLev = lev;
    }


    public static void v(Object obj) {
        if (MLev.lev <= LogLev.V.lev) {
            printLog(TAG, LogLev.V, getMsg(obj), null);
        }
    }


    public static void v(String tag, Object obj) {

        if (MLev.lev <= LogLev.V.lev) {
            printLog(tag, LogLev.V, getMsg(obj), null);
        }
    }


    public static void d(Object obj) {
        if (MLev.lev <= LogLev.D.lev) {
            printLog(TAG, LogLev.D, getMsg(obj), null);
        }
    }


    public static void d(String tag, Object obj) {

        if (MLev.lev <= LogLev.D.lev) {
            printLog(tag, LogLev.D, getMsg(obj), null);
        }
    }


    public static void i(Object obj) {
        if (MLev.lev <= LogLev.I.lev) {
            printLog(TAG, LogLev.I, getMsg(obj), null);
        }
    }


    public static void i(String tag, Object obj) {

        if (MLev.lev <= LogLev.I.lev) {
            printLog(tag, LogLev.I, getMsg(obj), null);
        }
    }


    public static void w(Object obj) {
        if (MLev.lev <= LogLev.W.lev) {
            printLog(TAG, LogLev.W, getMsg(obj), null);
        }
    }


    public static void w(String tag, Object obj) {

        if (MLev.lev <= LogLev.W.lev) {
            printLog(tag, LogLev.W, getMsg(obj), null);
        }
    }


    public static void e(Object obj) {
        if (MLev.lev <= LogLev.E.lev) {
            printLog(TAG, LogLev.E, getMsg(obj), null);
        }
    }


    public static void e(String tag, Object obj) {

        if (MLev.lev <= LogLev.E.lev) {
            printLog(tag, LogLev.E, getMsg(obj), null);
        }
    }


    /**
     * 避免和 e(String tag, Object obj) 冲突，捕捉异常时只能打印 String
     */
    public static void e(String str, Throwable tr) {
        if (MLev.lev <= LogLev.E.lev) {
            printLog(TAG, LogLev.E, getMsg(str), tr);
        }
    }


    public static void e(String tag, Object obj, Throwable tr) {

        if (MLev.lev <= LogLev.E.lev) {
            printLog(tag, LogLev.E, getMsg(obj), tr);
        }
    }


    /**
     * 日志打印
     *
     * @param lev 级别
     * @param msg Message
     */
    private static void printLog(String defaultTag, LogLev lev, String msg, Throwable tr) {

        // 记录下全局 TAG
        String tmpTAG = TAG;
        TAG = defaultTag;

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        byte index = 4;
        String fileName = stackTrace[index].getFileName();
        String className = stackTrace[index].getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();
        String tag = "[%s.%s(%s:%d)]";
        tag = String.format(Locale.getDefault(), tag, className, methodName, fileName, lineNumber);
        tag = TextUtils.isEmpty(TAG) ? tag : TAG + ":" + tag;

        switch (lev) {
            case V:
                Log.v(tag, msg);
                break;
            case D:
                Log.d(tag, msg);
                break;
            case I:
                Log.i(tag, msg);
                break;
            case W:
                Log.w(tag, msg);
                break;
            case E:
                if (tr == null) {
                    Log.e(tag, msg);
                } else {
                    Log.e(tag, msg, tr);
                }

                break;
            case NO_LOG:
                break;
        }

        // 恢复全局 TAG
        TAG = tmpTAG;
    }


    private static String getMsg(Object obj) {
        return obj != null && obj.toString() != null ? obj.toString() : "null";
    }


    static {
        MLev = LogLev.V;
    }


    private enum LogLev {
        V(1),
        D(2),
        I(3),
        W(4),
        E(5),
        NO_LOG(6);

        int lev = 1;


        LogLev(int i) {
            this.lev = i;
        }
    }
}

