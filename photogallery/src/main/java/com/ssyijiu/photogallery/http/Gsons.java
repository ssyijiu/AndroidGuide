package com.ssyijiu.photogallery.http;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by ssyijiu on 2016/8/16.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class Gsons {

    private Gsons() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("Gsons cannot be instantiated !");
    }

    private static final Gson GSON = new Gson();

    public static <T> T json2Bean(String json, Class<T> beanClass) {
        T bean = null;
        try {
            bean = GSON.fromJson(json, beanClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public static <T> T json2Bean(String json, Type type) {
        T bean = null;
        try {
            bean = GSON.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }
}
