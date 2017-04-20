package com.ssyijiu.criminalintent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class CrimeActivity extends BaseActivity {

    @Override protected int getContentView() {
        return R.layout.activity_crime;
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = new CrimeFragment();
            fm.beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit();
        }
    }

}
