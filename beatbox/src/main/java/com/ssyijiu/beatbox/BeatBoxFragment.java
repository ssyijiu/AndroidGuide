package com.ssyijiu.beatbox;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.ssyijiu.beatbox.app.BaseFragment;
import com.ssyijiu.beatbox.bean.BeatBox;
import com.ssyijiu.beatbox.recycleradapter.SoundAdapter;
import com.ssyijiu.common.handler.SafetyHandler;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.ToastUtil;

/**
 * Created by ssyijiu on 2017/5/12.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class BeatBoxFragment extends BaseFragment {

    private BeatBox mBeatBox;

    @Override protected int getFragLayoutId() {
        return R.layout.fragment_beat_box;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);  // 保留 fragment
        mBeatBox = new BeatBox();
        MLog.i("onCreate");
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        RecyclerView mRecyclerView = findView(R.id.rv_beat_box);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds(), mBeatBox));
    }


    @Override public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }

    public static Fragment newInstance() {
        return new BeatBoxFragment();
    }
}
