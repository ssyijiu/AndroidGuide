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

    private BitmapUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("BitmapUtil cannot be instantiated !");
    }

    /**
     * 获取压缩后的 Bitmap
     *
     * @param path 图片路径
     * @param activity 当前 Activity，用于计算压缩比
     * @return 压缩后的 Bitmap
     */
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
            .getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }


    /**
     * 获取压缩后的 Bitmap
     *
     * @param path 图片路径
     * @param destWidth Bitmap 目标宽度，用于计算压缩比
     * @param destHeight Bitmap 目标高度，用于计算压缩比
     * @return 压缩后的 Bitmap
     */
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


    /**
     * 将 View 转换为 Bitmap(截屏)
     */
    public static Bitmap view2Bitmap(View view) {

        // 1. 绘图缓存可用
        view.setDrawingCacheEnabled(true);
        // 2. 设置绘图缓存的质量
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        // 3. 获取缓存图片
        return view.getDrawingCache();
    }
}
