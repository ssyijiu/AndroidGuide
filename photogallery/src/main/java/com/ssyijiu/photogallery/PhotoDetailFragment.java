package com.ssyijiu.photogallery;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import com.github.chrisbanes.photoview.PhotoView;
import com.ssyijiu.photogallery.app.BaseFragment;
import com.ssyijiu.photogallery.image.Vinci;

/**
 * Created by ssyijiu on 2017/7/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoDetailFragment extends BaseFragment {

    private static final String ARG_URL = "arg_url";
    private static final String ARG_DATE = "arg_date";
    private String mUrl;
    private String mDate;

    @Override protected int getFragLayoutId() {
        return R.layout.fragment_photot_detail;
    }

    @Override protected void parseArguments(Bundle arguments) {
        super.parseArguments(arguments);
        mUrl = arguments.getString(ARG_URL);
        mDate = arguments.getString(ARG_DATE);
    }

    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {
        mContext.setTitle(mDate.split("T")[0]);
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


    public static PhotoDetailFragment newInstance(String url,String date) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

}
