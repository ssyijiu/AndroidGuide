package com.ssyijiu.common.util;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.ssyijiu.common.Common;
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


    public static String getPackageName() {
        return Common.getContext().getPackageName();
    }


    public static String getVersionName() {
        PackageManager packageManager = Common.getContext().getPackageManager();
        String packageName = Common.getContext().getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
            // can't reach
        }
        return "1.0.0";
    }


    public static int getVersionCode() {
        PackageManager packageManager = Common.getContext().getPackageManager();
        String packageName = Common.getContext().getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
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
}
