package com.ssyijiu.photogallery.image;//package com.ssyijiu.picassof.imageloader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by ssyijiu on 2016/12/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

class FrescoLoader implements ImageLoader {

    private FrescoLoader() {
    }


    static final FrescoLoader INSTANCE = new FrescoLoader();


    @Override public void loadImage(Context context, String url, ImageView imageView) {
        ((ImageVinci) imageView).setImageURI(url);
    }
}
