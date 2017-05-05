package com.ssyijiu.common.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.ssyijiu.common.Common;
import java.util.UUID;

/**
 * Created by ssyijiu on 2017/2/20.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class DeviceUtil {

    private DeviceUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("DeviceUtil cannot be instantiated !");
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

        StringBuilder uniqueId = new StringBuilder();

        // deviceId
        if (tm != null) {
            if (CommonUtil.checkPermission(Manifest.permission.READ_PHONE_STATE)) {
                uniqueId.append(tm.getDeviceId());
            }
        }

        // ANDROID_ID
        uniqueId.append(Settings.Secure.ANDROID_ID);

        // SIM
        uniqueId.append(PhoneUtil.getSimNum());

        // SERIAL
        uniqueId.append(Build.SERIAL);

        String id = MD5Util.getMD5(uniqueId.toString());
        id = id.substring(0, 16);

        sSP.edit().putString("uniqueId", id).apply();
        return id;
    }


    public static String getUniqueId() {
        String uniqueId = sSP.getString("uniqueId", null);
        if (uniqueId == null) {
            return createUniqueId();
        }
        return uniqueId;
    }
}
