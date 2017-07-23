package com.ssyijiu.photogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

    private static final int LAST_VISIBLE = 8;
    private RecyclerView mRecyclerView;
    private GridLayoutManager layoutManager;
    private PhotoAdapter mAdapter;
    private List<MeiZhi.Results> mDatas = new ArrayList<>();
    private int mCellWidth;
    private ImageLoader<PhotoAdapter.ViewHolder> mImageLoader;

    private int mPage = 1;
    private MeiZhiTask mTask;
    private MeiZhiTask mTask1;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_photo_gallery;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保留 fragment 横竖屏切换不去重复请求数据
        setRetainInstance(true);
        requestMeiZhi(mPage);

        mImageLoader = new ImageLoader<>(new Handler());
        mImageLoader.setImageLoadListener(
            new ImageLoader.ImageLoadListener<PhotoAdapter.ViewHolder>() {
                @Override
                public void onImageLoadFinish(PhotoAdapter.ViewHolder target, Bitmap bitmap) {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    target.bindMeiZhi(drawable);
                }
            });

        // 在子线程中准备 Looper
        mImageLoader.start();
        mImageLoader.getLooper();
    }


    @Override protected void initViewAndData(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_photogallery);
        mCellWidth = DensityUtil.dp2px(77);

        mRecyclerView.getViewTreeObserver()
            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override public void onGlobalLayout() {
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = mRecyclerView.getWidth();
                    int cellNum = width / mCellWidth;
                    layoutManager = new GridLayoutManager(mContext, cellNum);
                    mRecyclerView.setLayoutManager(layoutManager);
                    if(mRecyclerView.getAdapter() == null) {
                        updateUI();
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
                final boolean isBottom = (lastVisiblePosition >= itemCount - LAST_VISIBLE);
                if (isBottom) {
                    requestMeiZhi(++mPage);
                }
            }
        });
    }


    private void requestMeiZhi(int page) {
        mTask = new MeiZhiTask() {
            @Override protected void afterMeiZhi(MeiZhi meiZhi) {
                mDatas.addAll(meiZhi.results);
                updateUI();
            }
        };

        mTask.execute(page);
    }


    private void updateUI() {
        if (isActive()) {
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
        mImageLoader.clearQueue();
        mRecyclerView = null;
    }


    @Override public void onDestroy() {
        super.onDestroy();
        mTask.cancel(false);
        mImageLoader.quit();
    }

}
