package com.ssyijiu.ceoquiz;

import android.app.Application;
import android.content.res.Resources;
import com.ssyijiu.common.Common;

/**
 * Created by ssyijiu on 2017/4/3.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public class App extends Application {

    private static App app;

    @Override public void onCreate() {
        super.onCreate();
        app = this;
        Common.init(this);
    }

    public static App getApp() {
        return app;
    }

    public static Resources getAppResources() {
        return app.getResources();
    }
}
