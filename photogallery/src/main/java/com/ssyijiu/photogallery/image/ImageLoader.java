package com.ssyijiu.photogallery.image;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

/**
 * Created by ssyijiu on 2017/1/9.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public interface ImageLoader {
    
    void loadImage(Context context, String url, ImageView imageView);
}
