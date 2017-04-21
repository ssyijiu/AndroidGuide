package com.ssyijiu.criminalintent.app;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.ssyijiu.common.log.MLog;

/**
 * Created by ssyijiu on 2017/4/20.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public abstract class BaseFragment extends Fragment {
    String className;

    private Unbinder unbinder;
    protected Activity context;


    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        className = getClass().getSimpleName();
        MLog.i(className + ": onAttach");
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLog.i(className + ": onCreate");
    }


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
        MLog.i(className + ": onCreateView");
        View rootView = inflater.inflate(getFragLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initViewAndData(rootView, savedInstanceState);
        return rootView;
    }


    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MLog.i(className + ": onViewCreated");
    }


    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MLog.i(className + ": onActivityCreated");
    }


    @Override public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        MLog.i(className + ": onViewStateRestored");
    }


    @Override public void onStart() {
        super.onStart();
        MLog.i(className + ": onStart");
    }


    @Override public void onResume() {
        super.onResume();
        MLog.i(className + ": onResume");
    }


    @Override public void onPause() {
        super.onPause();
        MLog.i(className + ": onPause");
    }


    @Override public void onStop() {
        super.onStop();
        MLog.i(className + ": onStop");
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        MLog.i(className + ": onDestroyView");
        unbinder.unbind();
    }


    @Override public void onDestroy() {
        super.onDestroy();
        MLog.i(className + ": onDestroy");
    }


    protected abstract int getFragLayoutId();

    protected abstract void initViewAndData(View rootView, Bundle savedInstanceState);

}
