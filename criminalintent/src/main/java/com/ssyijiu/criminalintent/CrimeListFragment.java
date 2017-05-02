package com.ssyijiu.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import butterknife.BindView;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.ToastUtil;
import com.ssyijiu.criminalintent.app.BaseFragment;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.bean.CrimeLab;
import com.ssyijiu.criminalintent.recycleradapter.CrimeAdapter;
import com.ssyijiu.criminalintent.util.RealmUtil;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssyijiu on 2017/4/21.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class CrimeListFragment extends BaseFragment implements View.OnClickListener {

    private static final String SAVED_SUBTITLE_VISIBLE = "saved_subtitle_visible";

    @BindView(R.id.rv_crime) RecyclerView recyclerCrime;
    ViewStub stubEmpty;

    private CrimeAdapter adapter;
    private boolean subtitleVisible;
    private List<Crime> mDatas = new ArrayList<>();


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_crime_list;
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        recyclerCrime.setLayoutManager(new LinearLayoutManager(context));
        stubEmpty = (ViewStub) rootView.findViewById(R.id.stub_empty);

        // ViewStub 点击事件
        stubEmpty.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override public void onInflate(ViewStub stub, View inflated) {
                inflated.setOnClickListener(CrimeListFragment.this);
            }
        });


        // CrimeLab.instance().queryAllCrimesAsync().addChangeListener(
        //     new RealmChangeListener<RealmResults<Crime>>() {
        //         @Override public void onChange(RealmResults<Crime> element) {
        //             mDatas.clear();
        //             mDatas.addAll(element);
        //             MLog.i("realm","queryAllCrimesAsync");
        //         }
        //     });
    }

    @Override public void onResume() {
        super.onResume();
        updateUI();
        // updateUIAsync();

    }


    // private void updateUIAsync() {
    //
    //     CrimeLab.instance().gc();
    //
    //     // 是否显示 Empty 视图
    //     if (mDatas.isEmpty()) {
    //         stubEmpty.setVisibility(View.VISIBLE);
    //     } else {
    //         stubEmpty.setVisibility(View.GONE);
    //     }
    //
    //     if (adapter == null) {
    //         adapter = new CrimeAdapter(mDatas, this);
    //         recyclerCrime.setAdapter(adapter);
    //     } else {
    //         adapter.notifyDataSetChanged();
    //     }
    //
    //     updateSubtitleAsync();
    //
    // }


    private void updateUI() {

        CrimeLab.instance().gc();

        // 是否显示 Empty 视图
        if (CrimeLab.instance().queryAllCrimes().isEmpty()) {
            stubEmpty.setVisibility(View.VISIBLE);
        } else {
            stubEmpty.setVisibility(View.GONE);
        }

        if (adapter == null) {
            adapter = new CrimeAdapter(CrimeLab.instance().queryAllCrimes(), this);
            recyclerCrime.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        updateSubtitle();

    }


    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, subtitleVisible);
    }


    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                newCrime();
                return true;
            case R.id.menu_item_show_subtitle:
                subtitleVisible = !subtitleVisible;
                context.invalidateOptionsMenu();
                // updateSubtitleAsync();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void newCrime() {
        final Crime crime = new Crime();
        CrimeLab.instance().insertOrUpdateCrime(crime);
        // 刚刚插入了一条数据，-1
        int position = CrimeLab.instance().size() - 1;
        Intent intent = CrimePagerActivity.newIntent(context, crime.id, position);
        startActivity(intent);
    }


    /** 更新子标题 */
    // private void updateSubtitleAsync() {
    //
    //     int crimeSize = mDatas.size();
    //
    //     String subtitle = getResources()
    //         .getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);
    //
    //     AppCompatActivity activity = (AppCompatActivity) context;
    //     ActionBar actionBar = activity.getSupportActionBar();
    //
    //     if (!subtitleVisible) {
    //         subtitle = null;
    //     }
    //
    //     if (actionBar != null) {
    //         actionBar.setSubtitle(subtitle);
    //     }
    // }

    /** 更新子标题 */
    private void updateSubtitle() {

        int crimeSize = CrimeLab.instance().queryAllCrimes().size();

        String subtitle = getResources()
            .getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);

        AppCompatActivity activity = (AppCompatActivity) context;
        ActionBar actionBar = activity.getSupportActionBar();

        if (!subtitleVisible) {
            subtitle = null;
        }

        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recycler_empty:
                newCrime();
                break;
            default:
        }
    }
}
