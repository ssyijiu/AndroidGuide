package com.ssyijiu.common.util;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardUtil implements ViewTreeObserver.OnGlobalLayoutListener {

    private final View mRootView;

    private boolean mIsKeyboardOpened;


    private KeyboardUtil(View activityRootView) {
        mRootView = activityRootView;
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    /**
     * init KeyboardUtil, use activity/fragment rootView
     *
     * @param rootView can't use the view of Inflate R.layout
     */
    public static KeyboardUtil with(View rootView) {
        return new KeyboardUtil(rootView);
    }


    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        mRootView.getWindowVisibleDisplayFrame(r);

        final int heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);
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
        mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
}  