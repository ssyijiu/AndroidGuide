package com.ssyijiu.photogallery.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by ssyijiu on 2017/1/9.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public interface ImageLoader {

    void init(Context context);
    void loadImage(String url, ImageView imageView);
    void loadImage(String url, ImageView imageView, ImageOptions options);
}
