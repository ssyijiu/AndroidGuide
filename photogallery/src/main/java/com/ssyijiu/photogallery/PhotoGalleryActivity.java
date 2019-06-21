package com.ssyijiu.photogallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ssyijiu.photogallery.app.SimpleFragmentActivity;

import java.util.List;

public class PhotoGalleryActivity extends SimpleFragmentActivity {

    private PhotoGalleryFragment photoGalleryFragment;


    @Override
    protected Fragment createFragment() {
        photoGalleryFragment = (PhotoGalleryFragment) PhotoGalleryFragment.newInstance();
        return photoGalleryFragment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        refreshTitle();
    }


    private void refreshTitle() {
        @SuppressLint("RestrictedApi")
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            if (fragments.contains(photoGalleryFragment) && photoGalleryFragment.isVisible()) {
                photoGalleryFragment.getActivity().setTitle("PhotoGallery");
            }
        }
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, PhotoGalleryActivity.class);
    }
}

