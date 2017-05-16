package com.ssyijiu.nerdlauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.ssyijiu.nerdlauncher.app.BaseFragment;

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
    }


    public static Fragment newInstance() {
        return new NerdLauncherFragment();
    }
}
