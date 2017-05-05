package com.ssyijiu.common.util;

import android.content.pm.PackageManager;
import android.os.Build;
import com.ssyijiu.common.Common;
import java.lang.reflect.Method;

/**
 * Created by ssyijiu on 2017/1/27.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public class CommonUtil {

    private CommonUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("CommonUtil cannot be instantiated !");
    }


    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }


    public static boolean checkPermission(String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(Common.getContext(), permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = Common.getContext().getPackageManager();
            if (pm.checkPermission(permission, Common.getContext().getPackageName()) ==
                PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
}
