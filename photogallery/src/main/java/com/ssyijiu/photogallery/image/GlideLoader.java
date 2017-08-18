package com.ssyijiu.photogallery.image;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ssyijiu.photogallery.R;
import com.ssyijiu.photogallery.app.App;

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
        loadImage(url, imageView, null);
    }


    @Override public void loadImage(String url, ImageView imageView, ImageOptions options) {

        RequestOptions requestOptions = new RequestOptions().apply(defaultOptions);
        if (options != null) {
            if (options.isSet(options.error())) {
                requestOptions.error(options.error());
            }
        }

        Glide.with(getContext(imageView))
            .load(url)
            .apply(requestOptions)
            .into(imageView);
    }


    private Context getContext(ImageView imageView) {
        Context context = imageView.getContext();
        if (context instanceof Activity) {
            if (!((Activity) context).isDestroyed()) {
                return context;
            }
        }
        return App.getContext();
    }
}
