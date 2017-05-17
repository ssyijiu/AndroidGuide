package com.ssyijiu.photogallery.app;

import android.app.Application;
import android.content.Context;
import com.squareup.leakcanary.LeakCanary;
import com.ssyijiu.common.Common;

/**
 * Created by ssyijiu on 2017/4/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class App extends Application {

    private static App sApp;


    @Override public void onCreate() {
        super.onCreate();
        Common.init(this);
        sApp = this;

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your sApp in this process.
            return;
        }
        LeakCanary.install(this);
    }


    public static Context getContext() {
        return sApp;
    }
}
