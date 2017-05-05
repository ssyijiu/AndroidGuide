package com.ssyijiu.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil implements ViewTreeObserver.OnGlobalLayoutListener {

    private final View mActivityRootView;

    private boolean mIsKeyboardOpened;


    private KeyboardUtil(View activityRootView) {
        mActivityRootView = activityRootView;
        mActivityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    /**
     * init KeyboardUtil
     *
     * @param activityRootView can't use the view of Inflate R.layout
     */
    public static KeyboardUtil with(View activityRootView) {
        return new KeyboardUtil(activityRootView);
    }


    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        mActivityRootView.getWindowVisibleDisplayFrame(r);

        final int heightDiff = mActivityRootView.getRootView().getHeight() - (r.bottom - r.top);
        if (!mIsKeyboardOpened && heightDiff > 100) {
            notifyOnSoftKeyboardOpened(heightDiff);
        } else if (mIsKeyboardOpened && heightDiff < 100) {
            notifyOnSoftKeyboardClosed();
        }

    }


    /**
     * getViewTreeObserver().removeGlobalOnLayoutListener
     * call in Activity/Fragment#onDestroy
     */
    public void removeKeyboardChangedListener() {
        mActivityRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }


    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        mIsKeyboardOpened = true;
        if (mSoftKeyboardChangedListener != null) {
            mSoftKeyboardChangedListener.onKeyboardOpened(keyboardHeightInPx);
        }
    }


    private void notifyOnSoftKeyboardClosed() {
        mIsKeyboardOpened = false;
        if (mSoftKeyboardChangedListener != null) {
            mSoftKeyboardChangedListener.onKeyboardClosed();
        }
    }


    public interface OnKeyboardChangedListener {
        void onKeyboardOpened(int keyboardHeightInPx);

        void onKeyboardClosed();
    }


    private OnKeyboardChangedListener mSoftKeyboardChangedListener;


    public void setOnKeyboardChangedListener(OnKeyboardChangedListener listener) {
        mSoftKeyboardChangedListener = listener;
    }


    /**
     * Hide the soft keyboard
     *
     * @param activity the current activity
     */
    public static void hideKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}  