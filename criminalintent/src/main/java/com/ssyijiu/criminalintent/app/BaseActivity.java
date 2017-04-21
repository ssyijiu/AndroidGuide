package com.ssyijiu.criminalintent.app;

import android.content.Context;
import android.content.Intent;
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

    protected Context context;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        context = this;
        className = getClass().getSimpleName();

        MLog.i(className + ": onCreate");

        ButterKnife.bind(this);

        if(getIntent() != null) {
            parseIntent(getIntent());
        }
        initFragment();
        initViewAndData(savedInstanceState);
    }


    protected void parseIntent(Intent intent) {}


    protected abstract int getContentView();

    protected abstract void initFragment();

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


    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        MLog.i(className + ": onRestoreInstanceState");
    }


    @Override protected void onPause() {
        super.onPause();
        MLog.i(className + ": onPause");
    }


    @Override protected void onStop() {
        super.onStop();
        MLog.i(className + ": onStop");
    }


    @Override protected void onRestart() {
        super.onRestart();
        MLog.i(className + ": onRestart");
    }



    @Override public boolean isFinishing() {
        MLog.i(className + ": isFinishing");
        return super.isFinishing();
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        MLog.i(className + ": onDestroy");
    }

}
