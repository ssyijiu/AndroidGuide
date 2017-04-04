package com.ssyijiu.common.util;

import android.view.View;

/**
 * Created by ssyijiu on 2017/2/16.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public abstract class OnNoDoubleClickListener implements View.OnClickListener {

    private static long lastClickTime;
    private static final int DOUBLE_CLICK_THRESHOLD = 1000;


    @Override public void onClick(View v) {

        long currentClickTime = System.currentTimeMillis();
        long timeD = currentClickTime - lastClickTime;
        lastClickTime = currentClickTime;

        if (timeD < DOUBLE_CLICK_THRESHOLD) {
            onClick();
        }
    }


    public abstract void onClick();
}
