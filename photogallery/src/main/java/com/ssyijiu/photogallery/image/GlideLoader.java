package com.ssyijiu.photogallery.image;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ssyijiu.photogallery.R;

/**
 * Created by ssyijiu on 2016/12/26.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

class GlideLoader implements ImageLoader {

    private GlideLoader() {
    }


    static final GlideLoader INSTANCE = new GlideLoader();


    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
            .load(url)
            .error(R.color.colorAccent)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView);
    }

}
