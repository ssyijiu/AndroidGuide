package com.ssyijiu.criminalintent.app;

import android.app.Application;
import com.ssyijiu.common.Common;
import io.realm.Realm;

/**
 * Created by ssyijiu on 2017/4/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Common.init(this);
        Realm.init(this);
    }
}
