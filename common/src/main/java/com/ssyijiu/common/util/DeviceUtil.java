package com.ssyijiu.common.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.ssyijiu.common.Common;
import com.ssyijiu.common.log.MLog;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by ssyijiu on 2017/2/20.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class DeviceUtil {

    private DeviceUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("NetUtil cannot be instantiated !");
    }


    private static final UUID sUUID = UUID.randomUUID();

    private static final SharedPreferences sSP = Common.getContext()
        .getSharedPreferences("createUniqueId", Context.MODE_PRIVATE);


    /**
     * get Android uniqueId, if DeviceId useless return uuid
     *
     * <p>Requires Permission:
     * {@link Manifest.permission#READ_PHONE_STATE}
     */
    private static String createUniqueId() {

        final TelephonyManager tm = (TelephonyManager) Common.getContext()
            .getSystemService(Context.TELEPHONY_SERVICE);

        String uniqueId;

        if (tm != null && TextUtils.isEmpty(tm.getDeviceId())) {
            uniqueId = tm.getDeviceId();
            MLog.i("DeviceId:" + uniqueId);
        } else {
            uniqueId = sUUID.toString();
            MLog.i("UUID:" + uniqueId);
        }
        sSP.edit().putString("uniqueId", uniqueId).apply();
        return uniqueId.substring(0, 16);
    }


    public static String getUniqueId() {
        String uniqueId = sSP.getString("uniqueId", null);
        if (uniqueId == null) {
            return createUniqueId();
        }
        return uniqueId;
    }


    /**
     * 获取设备的唯一标识
     * @return DeviceId 优先 Mac地址 优先 ANDROID_ID
     *
     * 7.0 无效
     */
    public static String _getUniqueId() {

        Context context = Common.getContext();

        try {
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(fstream, 1024);
                mac = in.readLine();
            } catch (IOException ignored) {
            } finally {
                try {
                    fstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            MLog.i(json.toString());
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

}
