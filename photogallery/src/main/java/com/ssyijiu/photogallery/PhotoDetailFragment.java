package com.ssyijiu.photogallery;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.transition.TransitionInflater;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
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



    @Override protected void parseArguments(Bundle arguments) {
        super.parseArguments(arguments);
        mUrl = arguments.getString(ARG_URL);
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {
        final PhotoView mPhotoView = (PhotoView) rootView.findViewById(R.id.photoView);
        ViewCompat.setTransitionName(mPhotoView, mUrl);
        if (isActive()) {
            Vinci.instance().loadImage(mUrl, mPhotoView);
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
