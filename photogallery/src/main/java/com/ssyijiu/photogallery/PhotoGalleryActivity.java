package com.ssyijiu.photogallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.ssyijiu.photogallery.app.SimpleFragmentActivity;
import com.ssyijiu.photogallery.recycleradapter.PhotoAdapter;

public class PhotoGalleryActivity extends SimpleFragmentActivity
    implements PhotoAdapter.OnRecyclerClickListener {

    private Fragment photoGalleryFragment;


    @Override protected Fragment createFragment() {
        photoGalleryFragment = PhotoGalleryFragment.newInstance();
        return photoGalleryFragment;
    }


    @Override public void OnRecyclerClick(PhotoAdapter.ViewHolder holder) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        PhotoDetailFragment photoFragment =
            (PhotoDetailFragment) fm.findFragmentByTag(PhotoDetailFragment.TAG);
        if (photoFragment == null) {
            photoFragment = PhotoDetailFragment.newInstance(holder.url);
            ft.add(R.id.fragment_container, photoFragment);
        } else if (!photoFragment.isAdded()) {
            ft.add(R.id.fragment_container, photoFragment);
        } else {
            ft.show(photoFragment);
        }

        ft.addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .hide(photoGalleryFragment)
            .commit();

    }
}
