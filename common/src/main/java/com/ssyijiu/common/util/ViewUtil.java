package com.ssyijiu.common.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ssyijiu on 2017/2/16.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class ViewUtil {

    private ViewUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("ViewUtil cannot be instantiated !");
    }


    /**
     * set view width = 0 and height = 0;
     */
    public static void setViewSize_0(View view) {
        setViewSize(view, 0, 0);
    }


    public static void setViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {

            layoutParams.width = width;
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }


    private static long lastClickTime;


    /**
     * 判断 View 是否被快速点击
     */
    public static boolean isDoubleClick() {
        long currentClickTime = System.currentTimeMillis();
        long timeD = currentClickTime - lastClickTime;
        lastClickTime = currentClickTime;
        return timeD < 1000;
    }
}
