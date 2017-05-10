package com.ssyijiu.criminalintent.app;

import android.app.Application;
import android.content.Context;
import com.squareup.leakcanary.LeakCanary;
import com.ssyijiu.common.Common;
import io.realm.Realm;

/**
 * Created by ssyijiu on 2017/4/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class App extends Application {

    private static App app;


    @Override public void onCreate() {
        super.onCreate();
        Common.init(this);
        Realm.init(this);
        app = this;

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }


    public static Context getContext() {
        return app;
    }
}
