package com.ssyijiu.criminalintent.util.image;

/**
 * Created by ssyijiu on 2016/12/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class Vinci {

    // 1. Vinci
    private Vinci() {
    }


    private static class Lazy {
        static final ImageLoader INSTANCE = GlideLoader.INSTANCE;

    }

    public static ImageLoader instance() {
        return Lazy.INSTANCE;
    }

}
