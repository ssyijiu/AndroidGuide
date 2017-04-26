package com.ssyijiu.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import java.util.Iterator;

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
        updateUI();
    }


    private void updateUI() {

        // 删除没有 title 的元素
        // 集合在遍历时删除只能使用 迭代器的方式
        for(Iterator<Crime> it = CrimeLab.get().getCrimeList().listIterator(); it.hasNext();) {
            Crime crime = it.next();
            if(TextUtils.isEmpty(crime.title)) {
                it.remove();
            }
        }

        // 是否显示 Empty 视图
        if(CrimeLab.get().getCrimeList().isEmpty()) {
            stubEmpty.setVisibility(View.VISIBLE);
        } else {
            stubEmpty.setVisibility(View.INVISIBLE);
        }

        if (adapter == null) {
            adapter = new CrimeAdapter(CrimeLab.get().getCrimeList(),this);
            recyclerCrime.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        updateSubtitle();

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
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private void newCrime() {Crime crime = new Crime();
        addCrime(crime);
        Intent intent = CrimePagerActivity.newIntent(context,crime.id,indexOfCrime(crime));
        startActivityForResult(intent,REQUEST_CODE_CRIME);
    }


    /** 更新子标题 */
    private void updateSubtitle() {

        int crimeSize = CrimeLab.get().getCrimeList().size();

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

    private void addCrime(Crime crime) {
        CrimeLab.get().getCrimeList().add(crime);
    }

    private int indexOfCrime(Crime crime) {
        return CrimeLab.get().getCrimeList().indexOf(crime);
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
