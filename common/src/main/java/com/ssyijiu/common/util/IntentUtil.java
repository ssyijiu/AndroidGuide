package com.ssyijiu.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.ssyijiu.common.Common;

/**
 * Created by ssyijiu on 2017/5/5.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class IntentUtil {

    private IntentUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("IntentUtil cannot be instantiated !");
    }


    /**
     * 检查意图是否可用
     */
    public static boolean checkIntentAvailable(Intent intent) {
        PackageManager packageManager = Common.getContext().getPackageManager();
        return packageManager.resolveActivity(intent,
            PackageManager.MATCH_DEFAULT_ONLY) != null;
    }


    /**
     * 分享文本
     *
     * @param extraText 要分享的文本
     * @param extraSubject 要分享的主题
     */
    public static void shareText(Context context, String extraText, String extraSubject, String extraChooser) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.putExtra(Intent.EXTRA_SUBJECT, extraSubject);
        context.startActivity(Intent.createChooser(intent, extraChooser));
    }
}
