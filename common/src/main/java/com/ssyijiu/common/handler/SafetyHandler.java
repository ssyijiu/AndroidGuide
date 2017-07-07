package com.ssyijiu.common.handler;

import android.os.Handler;
import java.lang.ref.WeakReference;

/**
 * Created by ssyijiu on 2017/7/7.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class SafetyHandler extends Handler {

    /**
     * 外部引用, 例如 Activity, Fragment, Dialog, View 等
     */
    protected WeakReference<Object> mTargetRef ;

    public SafetyHandler() {
    }

    public SafetyHandler(Object external) {
        this.mTargetRef = new WeakReference<>(external);
    }

    protected  <T> T getTarget() {
        if ( isTargetAlive() ) {
            return (T) mTargetRef.get() ;
        } else {
            removeCallbacksAndMessages(null);
            return null;
        }

    }


    protected boolean isTargetAlive() {
        return mTargetRef != null && mTargetRef.get() != null;
    }

}
