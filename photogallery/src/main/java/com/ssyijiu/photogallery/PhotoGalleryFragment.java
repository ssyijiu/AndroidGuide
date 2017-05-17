package com.ssyijiu.photogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.DensityUtil;
import com.ssyijiu.common.util.ToastUtil;
import com.ssyijiu.photogallery.app.BaseFragment;
import com.ssyijiu.photogallery.bean.MeiZhi;
import com.ssyijiu.photogallery.http.MeiZhiTask;
import com.ssyijiu.photogallery.recycleradapter.PhotoAdapter;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.width;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoGalleryFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private GridLayoutManager layoutManager;
    private PhotoAdapter mAdapter;
    private List<MeiZhi.Results> mDatas = new ArrayList<>();
    private int mCellWidth;

    private int mPage = 1;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_photo_gallery;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保留 fragment 横竖屏切换不去重复请求数据
        setRetainInstance(true);

        // requestData(mPage);
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        mRecyclerView = findView(R.id.rv_photogallery);
        mCellWidth = DensityUtil.dp2px(77);

        final ViewTreeObserver observer = mRecyclerView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                int width = mRecyclerView.getWidth();
                int cellNum = width / mCellWidth;
                layoutManager = new GridLayoutManager(mContext, cellNum);
                mRecyclerView.setLayoutManager(layoutManager);
                requestData(mPage);
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) return;

                final int itemCount = layoutManager.getItemCount();
                final int lastVisiblePosition
                    = layoutManager.findLastCompletelyVisibleItemPosition();
                final boolean isBottom = (lastVisiblePosition >= itemCount - 1);
                if (isBottom) {
                    // requestData(++mPage);
                }
            }
        });
    }


    private void requestData(int page) {

        new MeiZhiTask() {
            @Override protected void afterMeiZhi(MeiZhi meiZhi) {
                setAdapter(meiZhi);
            }
        }.execute(page);
    }


    private void setAdapter(MeiZhi meiZhi) {

        if (isAdded()) {
            mDatas.addAll(meiZhi.results);
            if (mAdapter == null) {
                mAdapter = new PhotoAdapter(mDatas);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    public static Fragment newInstance() {
        return new PhotoGalleryFragment();
    }
}
