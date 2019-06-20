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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parseArguments(getArguments());
        }
    }

    protected void parseArguments(Bundle arguments) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getFragLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewAndData(view, savedInstanceState);
    }

    protected abstract int getFragLayoutId();

    protected abstract void initViewAndData(View rootView, Bundle savedInstanceState);

    public boolean isActive() {
        return mContext != null && isAdded();
    }
}
