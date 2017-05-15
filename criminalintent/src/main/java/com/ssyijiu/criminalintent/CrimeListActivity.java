package com.ssyijiu.criminalintent;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.DensityUtil;
import com.ssyijiu.common.util.ToastUtil;
import com.ssyijiu.criminalintent.app.SimpleFragmentActivity;
import com.ssyijiu.criminalintent.bean.Crime;

/**
 * Created by ssyijiu on 2017/4/21.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class CrimeListActivity extends SimpleFragmentActivity
    implements CrimeListFragment.OnCrimeListItemListener, CrimeFragment.OnCrimeUpdatedListener {

    private Crime currentCrime;


    @Override protected Fragment createFragment() {
        return new CrimeListFragment();
    }


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override protected int getLayoutResId() {

        // 没有平板，用手机横竖屏模拟
        if (DensityUtil.isScreenPortrait()) {
            return R.layout.activity_fragment;
        } else {
            return R.layout.activity_twopane;
        }

        // return R.layout.activity_masterdetail;
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        Point size = new Point();
        getWindowManager().getDefaultDisplay()
            .getSize(size);
        MLog.i(size.x+"");
    }


    @Override public void onCrimeItemClick(Crime crime, int position) {

        // 竖屏
        if (DensityUtil.isScreenPortrait()) {
            Intent intent = CrimePagerActivity.newIntent(context, crime.id, position);
            startActivity(intent);
        } else {

            if (currentCrime == crime) {
                return;
            }

            replaceDetail(crime.id, position);
            currentCrime = crime;
        }
    }


    private void replaceDetail(String id, int position) {
        Fragment newDetail = CrimeFragment.newInstance(id, position);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container_detailed, newDetail)
            .commit();
    }


    @Override public void onCrimeItemCheck(Crime crime, boolean isChecked) {

        if (crime == currentCrime) {
            CrimeFragment crimeFragment = (CrimeFragment)
                getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container_detailed);
            crimeFragment.updateCrimeSolved(isChecked);
        }

    }


    public boolean isPhone() {
        return findViewById(R.id.fragment_container_detailed) == null;
    }


    @Override public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment)
            getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();

    }
}
