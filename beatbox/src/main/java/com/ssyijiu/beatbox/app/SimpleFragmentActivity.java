package com.ssyijiu.beatbox.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.ssyijiu.beatbox.R;

/**
 * Created by ssyijiu on 2017/4/21.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public abstract class SimpleFragmentActivity extends BaseActivity {



    protected abstract Fragment createFragment();

    @Override protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }


    @Override protected void initFragment() {

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit();
        }
    }
}
