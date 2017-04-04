package com.ssyijiu.common.util;

/**
 * Created by ssyijiu on 2017/1/27.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public class CommonUtil {

    private CommonUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("CommonUtil cannot be instantiated !");
    }


    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }
}
