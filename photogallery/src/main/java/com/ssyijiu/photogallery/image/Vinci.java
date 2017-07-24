package com.ssyijiu.photogallery.image;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ssyijiu.photogallery.app.App;

/**
 * Created by ssyijiu on 2016/12/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class Vinci {

    private Vinci() {
    }

    public static void init(App app) {
        Fresco.initialize(app);
    }

    private static class Lazy {
        static final ImageLoader INSTANCE = FrescoLoader.INSTANCE;

    }

    public static ImageLoader instance() {
        return Lazy.INSTANCE;
    }

}
