package com.ssyijiu.common.util;

import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.ssyijiu.common.Common;

/**
 * Created by ssyijiu on 2017/7/7.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class DrawableUtil {

    private DrawableUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("DrawableUtil cannot be instantiated !");
    }


    public enum Direction {
        LEFT, TOP, RIGHT, BOTTOM
    }


    public static void setTextDrawable(TextView textView, Direction dir, int resId) {
        setTextDrawable(textView, dir, resId, 0);
    }


    public static void setTextDrawable(TextView textView, Direction dir, int resId, int drawablePadding) {
        if (resId == 0) {
            textView.setCompoundDrawables(null, null, null, null);
            return;
        }

        // 这里 id 不存在的话不会返回 null，直接抛出 NotFoundException
        Drawable drawable = Common.getAppResources().getDrawable(resId);
        setTextDrawable(textView, dir, drawable, drawablePadding);
    }


    public static void setTextDrawable(TextView textView, Direction dir, Drawable drawable) {
        setTextDrawable(textView, dir, drawable, 0);
    }


    /**
     * 设置 TextView 的 drawableLeft、drawableTop、drawableRight、drawableBottom
     * @param textView         要设置的 TextView
     * @param dir              方向
     * @param drawable         drawable 图片
     * @param drawablePadding  drawablePadding 间距
     */
    public static void setTextDrawable(TextView textView, Direction dir, Drawable drawable, int drawablePadding) {
        if (drawable == null || dir == null) {
            textView.setCompoundDrawables(null, null, null, null);
            return;
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawablePadding(drawablePadding);

        switch (dir) {
            case LEFT:
                textView.setCompoundDrawables(drawable, null, null, null);
                break;
            case TOP:
                textView.setCompoundDrawables(null, drawable, null, null);
                break;
            case RIGHT:
                textView.setCompoundDrawables(null, null, drawable, null);
                break;
            case BOTTOM:
                textView.setCompoundDrawables(null, null, null, drawable);
                break;
        }

    }

}
