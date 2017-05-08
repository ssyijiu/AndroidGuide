package com.ssyijiu.criminalintent.app;

import android.app.Application;
import android.content.Context;
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
    }


    public static Context getContext() {
        return app;
    }
}
