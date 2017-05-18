package com.ssyijiu.photogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import com.ssyijiu.common.util.DensityUtil;
import com.ssyijiu.photogallery.app.BaseFragment;
import com.ssyijiu.photogallery.bean.MeiZhi;
import com.ssyijiu.photogallery.http.ImageLoader;
import com.ssyijiu.photogallery.http.MeiZhiTask;
import com.ssyijiu.photogallery.recycleradapter.PhotoAdapter;
import java.util.ArrayList;
import java.util.List;

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
    private ImageLoader<PhotoAdapter.PhotoHolder> mImageLoader;

    private int mPage = 1;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_photo_gallery;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保留 fragment 横竖屏切换不去重复请求数据
        setRetainInstance(true);
        requestData(mPage);

        mImageLoader = new ImageLoader<>();

        // 在子线程中准备 Looper
        mImageLoader.start();
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_photogallery);
        mCellWidth = DensityUtil.dp2px(77);

        final ViewTreeObserver observer = mRecyclerView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = mRecyclerView.getWidth();
                int cellNum = width / mCellWidth;
                layoutManager = new GridLayoutManager(mContext, cellNum);
                mRecyclerView.setLayoutManager(layoutManager);

                // 横竖屏旋转后没有 setAdapter，给设置上
                if (mRecyclerView.getAdapter() == null) {
                    mAdapter = new PhotoAdapter(mDatas, mImageLoader);
                    mRecyclerView.setAdapter(mAdapter);
                }
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
                    requestData(++mPage);
                }
            }
        });
    }


    private void requestData(int page) {

        new MeiZhiTask() {
            @Override protected void afterMeiZhi(MeiZhi meiZhi) {
                mDatas.addAll(meiZhi.results);
                updateUI();
            }
        }.execute(page);
    }


    private void updateUI() {

        if (isAdded()) {
            if (mAdapter == null) {
                mAdapter = new PhotoAdapter(mDatas, mImageLoader);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    public static Fragment newInstance() {
        return new PhotoGalleryFragment();
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        mImageLoader.quit();
    }
}
