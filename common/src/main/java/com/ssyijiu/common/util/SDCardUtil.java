package com.ssyijiu.common.util;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;
import com.ssyijiu.common.Common;
import com.ssyijiu.common.log.MLog;
import java.io.File;

/**
 * Created by ssyijiu on 2016/10/10.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */
public class SDCardUtil {

    private SDCardUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("SDCardUtil cannot be instantiated");
    }


    /**
     * 判断SDCard是否可用
     *
     * @return true 可用，false 不可用
     */
    public static boolean isAvailable() {
        String state = Environment.getExternalStorageState();
        return state != null && state.equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取SD卡路径
     *
     * @return sdcard path
     */
    public static String getSDCardPath() {
        if (!isAvailable()) {
            return "the sdcard is unable!";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }


    /**
     * 获取SD卡的可用容量 单位byte
     *
     * @return the byte sdcard can use
     */
    public static long getUsableSize() {
        if (isAvailable()) {
            File file = new File(getSDCardPath());
            return file.getUsableSpace();
        }
        return 0;
    }


    /**
     * 获取SD卡的可用容量
     */
    public static String getUsableSize(Context context) {
        return Formatter.formatFileSize(context, getUsableSize());
    }


    /**
     * 获取磁盘缓存
     *
     * @param name 缓存的文件或者目录名称
     * @return sd卡可用路径为   /sdcard/Android/data/package_name/cache/fileName <br/>
     * sd卡不可用路径为 /data/data/package_name/cache/fileName
     */
    public static File getDiskCache(String name) {
        String cachePath;
        if (!isAvailable()) {
            cachePath = Common.getContext().getCacheDir().getAbsolutePath();
        } else {

            StringBuilder sb = new StringBuilder();
            File file = Common.getContext().getExternalCacheDir();

            // In some case, getExternalCacheDir will return null
            if (file != null) {
                sb.append(file.getAbsolutePath());
            } else {
                sb.append(getSDCardPath())
                    .append("Android/data/")
                    .append(Common.getContext().getPackageName())
                    .append("/cache");
            }

            cachePath = sb.toString();

            File cacheFile = new File(cachePath);
            if(!cacheFile.exists()) {
                if(!cacheFile.mkdirs()) {
                    return Common.getContext().getCacheDir();
                }
            }
        }

        return new File(cachePath + File.separator + name);
    }
}
