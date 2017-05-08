package com.ssyijiu.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.ssyijiu.criminalintent.app.BaseActivity;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.db.CrimeDao;
import java.util.List;

/**
 * Created by ssyijiu on 2017/4/23.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public class CrimePagerActivity extends BaseActivity {

    @BindView(R2.id.crime_pager_root) ViewPager viewPagerRoot;

    private List<Crime> mDatas;

    private static final String EXTRA_CRIME_ID = "extra_crime_id";
    private static final String EXTRA_CRIME_POSITION = "extra_crime_position";
    private String crimeId;
    private int currentPosition;


    @Override protected void parseIntent(Intent intent) {
        crimeId = intent.getStringExtra(EXTRA_CRIME_ID);
        currentPosition = intent.getIntExtra(EXTRA_CRIME_POSITION, 0);
    }


    @Override protected int getContentView() {
        return R.layout.activity_crime_pager;
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {


        // 同步查询
        mDatas = CrimeDao.instance().queryAllCrimes();
        setViewPager();

    }


    private void setViewPager() {

        final FragmentManager fragmentManager = getSupportFragmentManager();

        viewPagerRoot.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override public Fragment getItem(int position) {

                Crime crime = mDatas.get(position);
                return CrimeFragment.newInstance(crime.id,position);
            }

            @Override public int getCount() {
                return mDatas.size();
            }


            // fix bug : android.os.Handler android.support.v4.app.FragmentHostCallback.getHandler()' on a null object reference
            // v4' bug
            // http://blog.csdn.net/shineflowers/article/details/64125260
            @Override
            public void finishUpdate(ViewGroup container) {
                try{
                    super.finishUpdate(container);
                } catch (NullPointerException ignored){
                }
            }
        });
        viewPagerRoot.setCurrentItem(currentPosition);
    }


    public static Intent newIntent(Context context, String crimeId, int position) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_CRIME_POSITION, position);
        return intent;
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override public int getCount() {
            return 0;
        }


        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }


        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
