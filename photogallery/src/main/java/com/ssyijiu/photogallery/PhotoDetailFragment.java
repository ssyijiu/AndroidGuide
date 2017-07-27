package com.ssyijiu.photogallery;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.transition.TransitionInflater;
import android.view.View;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.ssyijiu.photogallery.app.BaseFragment;
import com.ssyijiu.photogallery.image.Vinci;

/**
 * Created by ssyijiu on 2017/7/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoDetailFragment extends BaseFragment {

    public static final String TAG = "PhotoDetailFragment";
    private static final String ARG_URL = "arg_url";
    private String mUrl;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_photot_detail;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fragment 共享元素
        // http://blog.csdn.net/u012403246/article/details/49942679
        // 1. 上一个 fragment setTransitionName
        // 2. 这个 fragment setTransitionName
        // 3
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(
                TransitionInflater.from(mContext)
                    .inflateTransition(android.R.transition.fade));
        }
        // 4. FragmentTransaction.addSharedElement(view,transitionName);
    }


    @Override protected void parseArguments(Bundle arguments) {
        super.parseArguments(arguments);
        mUrl = arguments.getString(ARG_URL);
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {
        PhotoView mPhotoView = (PhotoView) rootView.findViewById(R.id.photoView);
        if (isActive()) {
            Vinci.instance().loadImage(mUrl, mPhotoView);
            ViewCompat.setTransitionName(mPhotoView, mUrl);
        }

        rootView.findViewById(R.id.photoView_root).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mContext.onBackPressed();
            }
        });
    }


    public static PhotoDetailFragment newInstance(String url) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

}
