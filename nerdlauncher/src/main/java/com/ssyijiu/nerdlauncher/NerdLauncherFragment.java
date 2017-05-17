package com.ssyijiu.nerdlauncher;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.ssyijiu.common.util.AppUtil;
import com.ssyijiu.nerdlauncher.app.App;
import com.ssyijiu.nerdlauncher.app.BaseFragment;
import com.ssyijiu.nerdlauncher.recycleradapter.ActivityAdapter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ssyijiu on 2017/5/16.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class NerdLauncherFragment extends BaseFragment {

    private RecyclerView mRecyclerView;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_nerd_launcher;
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        mRecyclerView = findView(R.id.rv_nerd_launcher);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        setupAdapter();
    }


    private void setupAdapter() {
        List<ResolveInfo> activities = AppUtil.getAllLauncher();

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = App.getContext().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                    a.loadLabel(pm).toString(),
                    b.loadLabel(pm).toString());
            }
        });

        mRecyclerView.setAdapter(new ActivityAdapter(activities, mContext));
    }


    public static Fragment newInstance() {
        return new NerdLauncherFragment();
    }
}