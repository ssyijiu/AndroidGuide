package com.ssyijiu.photogallery;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.transition.TransitionInflater;
import com.ssyijiu.photogallery.app.SimpleFragmentActivity;
import com.ssyijiu.photogallery.recycleradapter.PhotoAdapter;

public class PhotoGalleryActivity extends SimpleFragmentActivity {

    @Override protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}

