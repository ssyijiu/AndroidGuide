package com.ssyijiu.common.handler;

import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

/**
 * Created by ssyijiu on 2017/7/7.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */


/**
 * 解决内存泄漏的 Handler，注意复写 {@link Handler#handleMessage(Message)}
 * @param <T> 外部引用
 *
 */
public class SafetyHandler<T> extends Handler {

    /**
     * 外部引用, 例如 Activity, Fragment, Dialog, View 等
     */
    private WeakReference<T> mTargetRef;


    public SafetyHandler() {
    }


    public SafetyHandler(T target) {
        this.mTargetRef = new WeakReference<>(target);
    }


    public T getTarget() {
        if (isTargetAlive()) {
            return mTargetRef.get();
        } else {
            removeCallbacksAndMessages(null);
            return null;
        }

    }


    private boolean isTargetAlive() {
        return mTargetRef != null && mTargetRef.get() != null;
    }


    public void setTarget(T target) {
        this.mTargetRef = new WeakReference<>(target);
    }
}
