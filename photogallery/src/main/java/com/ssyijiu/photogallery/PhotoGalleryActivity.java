package com.ssyijiu.photogallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.transition.TransitionInflater;
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
            photoFragment.setSharedElementEnterTransition(
                TransitionInflater.from(mContext)
                    .inflateTransition(android.R.transition.slide_bottom));
            photoFragment.setSharedElementReturnTransition(
                TransitionInflater.from(mContext)
                    .inflateTransition(android.R.transition.slide_top));

            String transitionName = ViewCompat.getTransitionName(holder.imageView);
            ft.add(R.id.fragment_container, photoFragment)
                .hide(photoGalleryFragment)
                .addSharedElement(holder.imageView,transitionName)
                .addToBackStack(null)
                .commit();
        }



    }
}
