package com.ssyijiu.photogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.ssyijiu.photogallery.app.BaseFragment;
import com.ssyijiu.photogallery.bean.MeiZhi;
import com.ssyijiu.photogallery.http.MeiZhiTask;
import com.ssyijiu.photogallery.recycleradapter.PhotoAdapter;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoGalleryFragment extends BaseFragment {

    private RecyclerView mRecyclerView;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_photo_gallery;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保留 fragment 横竖屏切换不去重复请求数据
        setRetainInstance(true);

        new MeiZhiTask() {
            @Override protected void afterGetMeiZhi(MeiZhi meiZhi) {
                if(isAdded()) {
                    mRecyclerView.setAdapter(new PhotoAdapter(meiZhi.results));
                }
            }
        }.execute();
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        mRecyclerView = findView(R.id.rv_photogallery);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    public static Fragment newInstance() {
        return new PhotoGalleryFragment();
    }
}
