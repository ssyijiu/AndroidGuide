package com.ssyijiu.ceoquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.ssyijiu.common.log.MLog;

/**
 * Created by ssyijiu on 2017/4/5.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public abstract class BaseActivity extends AppCompatActivity {

    String className;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        className = getClass().getSimpleName();

        MLog.i(className + ": onCreate");

        ButterKnife.bind(this);
        initViewAndData(savedInstanceState);
    }


    protected abstract int getContentView();

    protected abstract void initViewAndData(Bundle savedInstanceState);


    @Override protected void onStart() {
        super.onStart();
        MLog.i(className + ": onStart");
    }

    @Override protected void onResume() {
        super.onResume();
        MLog.i(className + ": onResume");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MLog.i(className + ": onSaveInstanceState");
    }


    @Override protected void onPause() {
        super.onPause();
        MLog.i(className + ": onPause");
    }


    @Override protected void onStop() {
        super.onStop();
        MLog.i(className + ": onStop");
    }


    @Override public void finish() {
        MLog.i(className + ": finish");
        super.finish();
    }


    @Override public boolean isFinishing() {
        MLog.i(className + ": isFinishing");
        return super.isFinishing();
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        MLog.i(className + ": isFinishing:" + isFinishing());
        MLog.i(className + ": onDestroy");
    }

}
