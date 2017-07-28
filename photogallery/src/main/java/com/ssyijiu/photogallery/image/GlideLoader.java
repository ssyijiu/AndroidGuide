package com.ssyijiu.photogallery.image;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ssyijiu.photogallery.R;

/**
 * Created by ssyijiu on 2016/12/26.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

class GlideLoader implements ImageLoader {

    static final GlideLoader INSTANCE = new GlideLoader();
    private RequestOptions defaultOptions;


    private GlideLoader() {
    }

    @Override public void init(Context context) {
        defaultOptions = new RequestOptions()
            .centerCrop()
            .error(R.color.colorAccent)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.IMMEDIATE);
    }


    @Override public void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
            .load(url)
            .apply(defaultOptions)
            .into(imageView);
    }


    @Override public void loadImage(String url, ImageView imageView, ImageOptions options) {

        if (options.has(options.error())) {
            defaultOptions.error(options.error());
        }

        Glide.with(imageView.getContext())
            .load(url)
            .apply(defaultOptions)
            .into(imageView);
    }

}
