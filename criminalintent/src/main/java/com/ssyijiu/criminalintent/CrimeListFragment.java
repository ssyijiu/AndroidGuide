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
import com.ssyijiu.criminalintent.app.BaseFragment;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.bean.CrimeLab;
import com.ssyijiu.criminalintent.recycleradapter.CrimeAdapter;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by ssyijiu on 2017/4/21.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class CrimeListFragment extends BaseFragment implements View.OnClickListener {

    public static final int REQUEST_CODE_CRIME = 0;
    private static final String SAVED_SUBTITLE_VISIBLE = "saved_subtitle_visible";

    @BindView(R.id.rv_crime) RecyclerView recyclerCrime;
    @BindView(R.id.stub_empty) ViewStub stubEmpty;


    private CrimeAdapter adapter;
    private boolean subtitleVisible;
    private RealmResults<Crime> mDatas;


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_crime_list;
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        recyclerCrime.setLayoutManager(new LinearLayoutManager(context));

        // ViewStub 点击事件
        stubEmpty.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override public void onInflate(ViewStub stub, View inflated) {
                inflated.setOnClickListener(CrimeListFragment.this);
            }
        });

        CrimeLab.instance().getAllCrimes().addChangeListener(
            new RealmChangeListener<RealmResults<Crime>>() {
                @Override public void onChange(RealmResults<Crime> element) {
                    mDatas = element;
                    updateUI();
                }
            });


    }


    private void updateUI() {


        if(mDatas.isEmpty()) {
            stubEmpty.setVisibility(View.VISIBLE);
        } else {
            stubEmpty.setVisibility(View.GONE);
        }

        if (adapter == null) {
            adapter = new CrimeAdapter(mDatas,CrimeListFragment.this);
            recyclerCrime.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        updateSubtitle(mDatas.size());

        // 是否显示 Empty 视图
        // if(CrimeLab.instance().getAllCrimes().isEmpty()) {
        //     stubEmpty.setVisibility(View.VISIBLE);
        // } else {
        //     stubEmpty.setVisibility(View.GONE);
        // }
        //
        // if (adapter == null) {
        //     adapter = new CrimeAdapter(CrimeLab.instance().getAllCrimes(),this);
        //     recyclerCrime.setAdapter(adapter);
        // } else {
        //     adapter.notifyDataSetChanged();
        // }
        //
        // updateSubtitle();

    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,subtitleVisible);
    }


    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CRIME
            && resultCode == CrimeFragment.RESULT_CODE_CRIME_POSITION) {
            int position = CrimeFragment.resultPosition(data);
            updateUI();

        }
    }


    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

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
                updateSubtitle(mDatas.size());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void newCrime() {
        Crime crime = new Crime();
        CrimeLab.instance().insertOrUpdateCrime(crime);
        int position = CrimeLab.instance().size() - 1;
        Intent intent = CrimePagerActivity.newIntent(context,crime.id,position);
        startActivityForResult(intent,REQUEST_CODE_CRIME);
    }


    /** 更新子标题 */
    private void updateSubtitle(int crimeSize) {

        // int crimeSize = CrimeLab.instance().getAllCrimes().size();

        String subtitle = getResources()
            .getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);

        AppCompatActivity activity = (AppCompatActivity) context;
        ActionBar actionBar = activity.getSupportActionBar();

        if(!subtitleVisible) {
            subtitle = null;
        }

        if(actionBar != null) {
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
