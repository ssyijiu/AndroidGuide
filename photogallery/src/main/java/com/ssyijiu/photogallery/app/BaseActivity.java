package com.ssyijiu.photogallery.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ssyijiu on 2017/4/5.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Activity mContext;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        mContext = this;

        if(getIntent() != null) {
            parseIntent(getIntent());
        }
        initFragment();
        initViewAndData(savedInstanceState);
    }


    protected void parseIntent(Intent intent) {}


    @LayoutRes
    protected abstract int getLayoutResId();

    protected void initFragment(){}

    protected void initViewAndData(Bundle savedInstanceState) {};

}
