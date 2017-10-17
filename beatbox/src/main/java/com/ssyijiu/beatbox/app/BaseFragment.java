package com.ssyijiu.beatbox.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ssyijiu on 2017/4/20.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public abstract class BaseFragment extends Fragment {
    protected Activity mContext;
    protected View mRootView;


    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parseArguments(getArguments());
        }
    }


    protected void parseArguments(Bundle arguments) {}


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getFragLayoutId(), container, false);
            initViewAndData(savedInstanceState);
        }
        return mRootView;
    }


    /**
     * 返回 Fragment 的布局
     * @return layoutId
     */
    protected abstract int getFragLayoutId();

    /**
     * 初始化 View 和 数据
     */
    protected abstract void initViewAndData(Bundle savedInstanceState);


    private SparseArray<View> mViews = new SparseArray<>();


    public <T extends View> T findView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mRootView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

}
