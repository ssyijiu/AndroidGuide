package com.ssyijiu.common.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import com.ssyijiu.common.Common;

/**
 * Created by ssyijiu on 2016/9/29.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */
public class PhoneUtil {

    private PhoneUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("PhoneUtil cannot be instantiated !");
    }


    private static TelephonyManager getTelephonyManager(Context context) {

        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }


    /**
     * 判断SIM卡是否可用
     * <br/> 权限: < uses-permission android:name = "android.permission.READ_PHONE_STATE" />
     */
    public static boolean isSimAvailable() {

        if (CommonUtil.checkPermission(Manifest.permission.READ_PHONE_STATE)) {
            return getTelephonyManager(Common.getContext()).getSimSerialNumber() != null;
        }

        return false;

    }


    /**
     * 获取SIM卡序列号
     * <br/> 权限: < uses-permission android:name = "android.permission.READ_PHONE_STATE" />
     *
     * @return SIM卡不存在返回 ""
     */
    public static String getSimNum() {
        if (isSimAvailable()) {
            return getTelephonyManager(Common.getContext()).getSimSerialNumber();
        }
        return "";
    }


    /**
     * go to Dial with phoneNum
     */
    public static void toDial(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }

}
