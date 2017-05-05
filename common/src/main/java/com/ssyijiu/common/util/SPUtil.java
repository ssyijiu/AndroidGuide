package com.ssyijiu.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.ssyijiu.common.Common;

/**
 * Created by ssyijiu on 2016/8/16.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 *
 * 批量提交数据请使用:
 * SPUtil.getEditor(context).putString(key,value).putInt(key,value).apply();
 *
 * 单次提交数据直接使用:SPUtil.putString();
 */
public class SPUtil {

    private static SharedPreferences sSP;
    private static SharedPreferences.Editor sEditor;


    private SPUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("SPUtil cannot be instantiated !");
    }


    private static SharedPreferences getInstance() {

        if (sSP == null) {
            sSP = Common.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sSP;
    }


    public static SharedPreferences.Editor getEditor(Context context) {
        if (sEditor == null) {
            sEditor = getInstance().edit();
        }
        return sEditor;
    }


    public static void putBoolean(String key, boolean value) {
        sSP = getInstance();
        sSP.edit().putBoolean(key, value).apply();
    }


    public static void putString(String key, String value) {
        sSP = getInstance();
        sSP.edit().putString(key, value).apply();
    }


    public static void putInt(String key, int value) {
        sSP = getInstance();
        sSP.edit().putInt(key, value).apply();
    }


    public static boolean getBoolean(String key, boolean defValue) {
        sSP = getInstance();
        return sSP.getBoolean(key, defValue);
    }


    public static String getString(String key, String defValue) {
        sSP = getInstance();
        return sSP.getString(key, defValue);
    }


    public static int getInt(String key, int defValue) {
        sSP = getInstance();
        return sSP.getInt(key, defValue);
    }


    public static void remove(String key) {
        sSP = getInstance();
        sSP.edit().remove(key).apply();
    }


    public static void clear(String key) {
        sSP = getInstance();
        sSP.edit().clear().apply();

    }


    public static boolean contains(String key) {
        sSP = getInstance();
        return sSP.contains(key);

    }


    public static void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (listener != null) {
            sSP = getInstance();
            sSP.registerOnSharedPreferenceChangeListener(listener);
        }

    }


    public static void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (listener != null) {
            sSP = getInstance();
            sSP.unregisterOnSharedPreferenceChangeListener(listener);
        }
    }

}
