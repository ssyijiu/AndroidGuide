package com.ssyijiu.beatbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.ssyijiu.beatbox.app.BaseFragment;
import com.ssyijiu.beatbox.bean.BeatBox;
import com.ssyijiu.beatbox.recycleradapter.SoundAdapter;

/**
 * Created by ssyijiu on 2017/5/12.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class BeatBoxFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private BeatBox mBeatBox;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_beat_box;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBeatBox = new BeatBox();
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        mRecyclerView = findView(R.id.rv_beat_box);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));
    }


    public static Fragment newInstance() {
        return new BeatBoxFragment();
    }
}
