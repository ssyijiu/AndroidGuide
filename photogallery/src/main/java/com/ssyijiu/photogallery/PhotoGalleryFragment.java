package com.ssyijiu.photogallery;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.DensityUtil;
import com.ssyijiu.photogallery.app.BaseFragment;
import com.ssyijiu.photogallery.bean.MeiZhi;
import com.ssyijiu.photogallery.http.MeiZhiTask;
import com.ssyijiu.photogallery.http.PollService;
import com.ssyijiu.photogallery.http.SearchTask;
import com.ssyijiu.photogallery.recycleradapter.PhotoAdapter;
import java.util.ArrayList;
import java.util.List;

import static com.ssyijiu.photogallery.tools.Preferences.loadQueryKey;
import static com.ssyijiu.photogallery.tools.Preferences.saveQueryKey;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoGalleryFragment extends BaseFragment {

    private static final int LAST_VISIBLE = 8;
    private static final int PAGE_START = 1;
    private RecyclerView mRecyclerView;
    private GridLayoutManager layoutManager;
    private PhotoAdapter mAdapter;
    private List<MeiZhi.Results> mDatas = new ArrayList<>();
    private int mCellWidth;

    private int mPage = PAGE_START;
    private MeiZhiTask mMeizhiTask;
    private SearchTask mSearchTask;
    private boolean search = false;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_photo_gallery;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保留 fragment 横竖屏切换不去重复请求数据
        setRetainInstance(true);
        setHasOptionsMenu(true);
        requestMeiZhi(mPage);
        PollService.start();
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
                    if (mRecyclerView.getAdapter() == null) {
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
                    if (search) {
                        searchMeiZhi(loadQueryKey(), String.valueOf(++mPage));
                    } else {
                        requestMeiZhi(++mPage);
                    }
                }
            }
        });
    }


    private void requestMeiZhi(int page) {
        search = false;
        mMeizhiTask = new MeiZhiTask() {
            @Override protected void afterMeiZhi(MeiZhi meiZhi) {
                updateData(meiZhi);
                updateUI();
            }
        };

        mMeizhiTask.execute(page);
    }


    private void searchMeiZhi(String queryKey, String page) {
        search = true;
        mSearchTask = new SearchTask() {
            @Override protected void afterSearch(MeiZhi meiZhi) {
                updateData(meiZhi);
                updateUI();
            }
        };

        mSearchTask.execute(queryKey, page);
    }


    private void updateData(MeiZhi meiZhi) {
        if (mPage == PAGE_START) {
            mDatas.clear();
        }
        mDatas.addAll(meiZhi.results);
    }


    private void updateUI() {
        if (isActive()) {
            if (mAdapter == null) {
                mAdapter = new PhotoAdapter(mDatas, mOnRecyclerClickListener);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    public static Fragment newInstance() {
        return new PhotoGalleryFragment();
    }


    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.meun_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                searchView.setQuery(loadQueryKey(), false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                MLog.i("onQueryTextSubmit:" + query);
                search(query, searchView);
                return true;
            }


            @Override public boolean onQueryTextChange(String newText) {
                MLog.i("onQueryTextChange:" + newText);
                return false;
            }
        });
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                if (search) {
                    mPage = PAGE_START;
                    requestMeiZhi(mPage);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView = null;
    }


    @Override public void onDestroy() {
        super.onDestroy();
        if (mMeizhiTask != null && !mMeizhiTask.isCancelled()) {
            mMeizhiTask.cancel(false);
        }

        if (mSearchTask != null && !mSearchTask.isCancelled()) {
            mSearchTask.cancel(false);
        }

    }


    /**
     * 开始搜索
     *
     * @param query 关键词
     * @param searchView searchView
     */
    private void search(String query, SearchView searchView) {
        mPage = PAGE_START;
        saveQueryKey(query);
        searchMeiZhi(loadQueryKey(), String.valueOf(mPage));
        searchView.setIconified(true);  // 点击 x 清空关键词
        searchView.setIconified(true);  // 再次点击 x 关闭 searchView 视图
    }




    private PhotoAdapter.OnRecyclerClickListener mOnRecyclerClickListener;


    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof PhotoAdapter.OnRecyclerClickListener) {
            mOnRecyclerClickListener = (PhotoAdapter.OnRecyclerClickListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                + " must implement View.OnClickListener");
        }
    }


    @Override public void onDetach() {
        super.onDetach();
        mOnRecyclerClickListener = null;
    }
}
