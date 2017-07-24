package com.ssyijiu.photogallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
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
    private ImageLoader<ImageView> mImageLoader;

    private int mPage = 1;
    private MeiZhiTask mTask;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_photo_gallery;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保留 fragment 横竖屏切换不去重复请求数据
        setRetainInstance(true);
        requestMeiZhi(mPage);

        mImageLoader = new ImageLoader<>(new Handler(Looper.getMainLooper()));
        mImageLoader.setImageLoadListener(
            new ImageLoader.LoadFinishListener<ImageView>() {
                @Override
                public void onImageLoadFinish(ImageView target, Bitmap bitmap) {
                    target.setImageBitmap(bitmap);
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
