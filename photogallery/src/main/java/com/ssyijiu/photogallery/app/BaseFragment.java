package com.ssyijiu.photogallery.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ssyijiu.common.util.ToastUtil;

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
        // if (mRootView == null) {
            mRootView = inflater.inflate(getFragLayoutId(), container, false);
            initViewAndData(savedInstanceState);
        // }
        return mRootView;
    }


    protected abstract int getFragLayoutId();

    protected abstract void initViewAndData(Bundle savedInstanceState);




    // fix bug：E/RecyclerView: No adapter attached; skipping layout
    // RecyclerView 被缓存了，横竖屏旋转后 findView 找到的依然是以前的，无法更新 UI
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
