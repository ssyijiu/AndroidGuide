package com.ssyijiu.photogallery;

import android.support.v4.app.Fragment;
import com.ssyijiu.photogallery.app.SimpleFragmentActivity;

public class PhotoGalleryActivity extends SimpleFragmentActivity {

    @Override protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
