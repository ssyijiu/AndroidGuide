package com.ssyijiu.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import com.ssyijiu.common.Common;
import java.io.File;
import java.util.List;

/**
 * Created by ssyijiu on 2016/9/29.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class AppUtil {

    private AppUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("AppUtil cannot be instantiated !");
    }


    private static PackageManager getPackageManager() {
        return Common.getContext().getPackageManager();
    }

    public static String getPackageName() {
        return Common.getContext().getPackageName();
    }


    public static String getVersionName() {
        String packageName = Common.getContext().getPackageName();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
            // can't reach
        }
        return "1.0.0";
    }


    public static int getVersionCode() {
        String packageName = Common.getContext().getPackageName();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
            // can't reach
        }
        return 1;
    }

    public static List<ResolveInfo> getAllLauncher() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PackageManager pm = Common.getContext().getPackageManager();
        return pm.queryIntentActivities(startupIntent, 0);
    }


    public static String getAppName() {
        String name = null;
        String packageName = Common.getContext().getPackageName();
        ApplicationInfo applicationInfo;
        try {
            PackageManager packageManager = Common.getContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            name = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return name;
    }

    public static Drawable getAppIcon() {
        Drawable icon = null;
        String packageName = Common.getContext().getPackageName();
        ApplicationInfo applicationInfo;
        try {
            PackageManager packageManager = Common.getContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            icon = packageManager.getApplicationIcon(applicationInfo);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return icon;
    }

    public static String getAppName(ResolveInfo resolveInfo) {
        return resolveInfo.loadLabel(getPackageManager()).toString();
    }


    public static Drawable getAppIcon(ResolveInfo resolveInfo) {
        return resolveInfo.loadIcon(getPackageManager());
    }

    public static void install(final File file) {

        if (file == null || !file.exists() || !file.isFile()) return;
        if (!checkApk(file.getAbsolutePath())) return;

        Context context = Common.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        // Android 7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && targetN()) {

            uri = FileProvider.getUriForFile(context, "com.ssyijiu.common.provider.install",
                file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 检查 apk 文件是否有效(是正确下载,没有损坏的)
     */
    public static boolean checkApk(String apkFilePath) {
        boolean result;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(apkFilePath,
                PackageManager.GET_ACTIVITIES);
            result = packageInfo != null;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    public static boolean targetN() {
        return Common.getContext().getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.N;
    }


    public static boolean targetM() {
        return Common.getContext().getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M;
    }
}
