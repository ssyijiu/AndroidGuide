package com.ssyijiu.common;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by ssyijiu on 2017/1/27.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public class Common {

    private static Application sApp;
    private static Resources sResources;


    public static void init(Application context) {
        sApp = context;
        sResources = context.getResources();
    }


    public static Context getContext() {
        return sApp;
    }


    public static Resources getAppResources() {
        return sResources;
    }
}
