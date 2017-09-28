package com.ssyijiu.photogallery.image;

import android.content.Context;

/**
 * Created by ssyijiu on 2016/12/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class Vinci {

    private Vinci() {
    }

    public static void init(Context context) {
        instance().init(context);
    }

    private static class Lazy {
        static final ImageLoader INSTANCE = GlideLoader.INSTANCE;

    }

    public static ImageLoader instance() {
        return Lazy.INSTANCE;
    }

}
