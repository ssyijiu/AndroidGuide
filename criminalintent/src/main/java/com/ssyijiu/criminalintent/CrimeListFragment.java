package com.ssyijiu.criminalintent;

import android.app.Activity;
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
import com.ssyijiu.common.util.DensityUtil;
import com.ssyijiu.criminalintent.app.BaseFragment;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.db.CrimeDao;
import com.ssyijiu.criminalintent.recycleradapter.CrimeAdapter;
import com.ssyijiu.criminalintent.util.RealmUtil;

/**
 * Created by ssyijiu on 2017/4/21.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class CrimeListFragment extends BaseFragment implements View.OnClickListener {

    private static final String SAVED_SUBTITLE_VISIBLE = "saved_subtitle_visible";

    @BindView(R2.id.rv_crime) RecyclerView recyclerCrime;
    ViewStub stubEmpty;

    private CrimeAdapter adapter;
    private boolean subtitleVisible;

    public OnCrimeListItemListener listener;

    /**
     * Required interface for hosting activities.
     */
    public interface OnCrimeListItemListener {
        void onCrimeItemClick(Crime crime, int position);
        void onCrimeItemCheck(Crime crime, boolean isChecked);
    }


    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnCrimeListItemListener) {
            listener = (OnCrimeListItemListener) activity;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnCrimeListItemListener");
        }

    }


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
                inflated.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        newCrime();
                    }
                });
            }
        });
    }


    @Override public void onResume() {
        super.onResume();
        CrimeDao.instance().gc();
        updateUI();
    }


    public void updateUI() {

        // 是否显示 Empty 视图
        if (CrimeDao.instance().queryAllCrimes().isEmpty()) {
            stubEmpty.setVisibility(View.VISIBLE);
            recyclerCrime.setVisibility(View.GONE);
        } else {
            stubEmpty.setVisibility(View.GONE);
            recyclerCrime.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new CrimeAdapter(CrimeDao.instance().queryAllCrimes(), this);
                recyclerCrime.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
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
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override public void onDestroy() {
        super.onDestroy();
        RealmUtil.getRealm().removeAllChangeListeners();

        // 由于数据使用了单例管理，关闭 Realm 后由于单例还保存在空进程的内存中
        // 会导致下次从空进程启动 app crash
        // RealmUtil.getRealm().close();
    }


    /** 更新子标题 */
    private void updateSubtitle() {

        int crimeSize = CrimeDao.instance().queryAllCrimes().size();
        // int crimeSize = mDatas.size();

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


    private void newCrime() {
        final Crime crime = new Crime();
        CrimeDao.instance().insertOrUpdateCrime(crime);
        if(DensityUtil.isScreenLand()) {
            updateUI();
        }
        int position = CrimeDao.instance().size() - 1;
        listener.onCrimeItemClick(crime, position);
    }


    @Override public void onDetach() {
        super.onDetach();
        if (listener != null) {
            listener = null;
        }
    }
}
