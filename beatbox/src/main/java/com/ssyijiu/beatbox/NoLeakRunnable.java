package com.ssyijiu.beatbox;

import com.ssyijiu.common.log.MLog;

/**
 * Created by ssyijiu on 2017/7/25.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class NoLeakRunnable implements Runnable {

    @Override public void run() {
        MLog.i("leak");
    }
}
