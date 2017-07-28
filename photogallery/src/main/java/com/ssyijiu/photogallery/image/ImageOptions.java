package com.ssyijiu.photogallery.image;

/**
 * Created by ssyijiu on 2017/7/27.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public class ImageOptions {

    private static final int UN_SET = -1;
    private int error = UN_SET;


    public ImageOptions error(int resourceId) {
        error = resourceId;
        return this;
    }


    int error() {
        return error;
    }


    boolean has(int flag) {
        return flag != UN_SET;
    }


    private static final ImageOptions defaultOptions = new ImageOptions();


    public static ImageOptions getOptions() {
        // clean all Options
        defaultOptions.error(UN_SET);
        return defaultOptions;
    }
}
