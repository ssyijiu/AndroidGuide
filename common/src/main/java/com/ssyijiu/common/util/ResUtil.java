package com.ssyijiu.common.util;

import android.graphics.drawable.Drawable;
import com.ssyijiu.common.Common;

/**
 * Created by ssyijiu on 2017/3/6.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class ResUtil {

    private ResUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("ResUtil cannot be instantiated !");
    }


    public static String getString(int id) {
        return Common.getAppResources().getString(id);
    }


    public static int getColor(int id) {
        return Common.getAppResources().getColor(id);
    }

    public static Drawable getDrawable(int id) {
        return Common.getAppResources().getDrawable(id);
    }
}
