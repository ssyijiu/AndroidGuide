package com.ssyijiu.common.util;

import android.text.TextUtils;

/**
 * Created by ssyijiu on 2017/2/8.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class StringUtil {

    private StringUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("StringUtil cannot be instantiated !");
    }


    /**
     * if null -> ""
     */
    public static String null2Empty(String str) {
        return null2Other(str, "");
    }


    /**
     * if null -> other
     */
    public static String null2Other(String str, String other) {
        if (TextUtils.isEmpty(str)) {
            return other;
        }
        return str;
    }
}
