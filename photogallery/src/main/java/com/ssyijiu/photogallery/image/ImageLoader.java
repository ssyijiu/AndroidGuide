package com.ssyijiu.photogallery.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by ssyijiu on 2017/1/9.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public interface ImageLoader {

    /**
     * 初始化 ImageLoader
     * @param context context
     */
    void init(Context context);

    /**
     * 加载图片
     * @param url  图片地址
     * @param imageView  ImageView
     */
    void loadImage(String url, ImageView imageView);

    /**
     * 加载图片
     * @param url  图片地址
     * @param imageView  ImageView
     * @param options  加载图片时的配置选项
     */
    void loadImage(String url, ImageView imageView, ImageOptions options);
}
