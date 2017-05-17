package com.ssyijiu.common.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import com.ssyijiu.common.Common;

import static android.R.attr.drawablePadding;
import static android.R.attr.translateX;

/**
 * Created by ssyijiu on 2017/5/9.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class BitmapUtil {

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
            .getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }


    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // Figure out how much to scale down by
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Read in and create final bitmap
        return BitmapFactory.decodeFile(path, options);
    }


    public static void setTextDrawableLeft(TextView textView, int resId) {
        setTextDrawableLeft(textView, resId, 0);
    }

    public static void setTextDrawableLeft(TextView textView, int resId, int drawablePadding) {
        if (resId == 0) {
            textView.setCompoundDrawables(null, null, null, null);
            return;
        }
        Drawable drawable = Common.getAppResources().getDrawable(resId);
        setTextDrawableLeft(textView, drawable, drawablePadding);
    }

    public static void setTextDrawableLeft(TextView textView, Drawable drawable) {
        setTextDrawableLeft(textView, drawable, 0);
    }

    public static void setTextDrawableLeft(TextView textView, Drawable drawable, int drawablePadding) {
        if (drawable == null) {
            textView.setCompoundDrawables(null, null, null, null);
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
        textView.setCompoundDrawablePadding(drawablePadding);
    }


    public static Bitmap getBitmapFromView(View view) {

        // 1. 绘图缓存可用
        view.setDrawingCacheEnabled(true);
        // 2. 设置绘图缓存的质量
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        // 3. 获取缓存图片
        return view.getDrawingCache();
    }
}
