package com.ssyijiu.common.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.ssyijiu.common.Common;

/**
 * Created by ssyijiu on 2016/8/16.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 * <p>
 * 需要权限：<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 */
public class NetUtil {

    private NetUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("NetUtil cannot be instantiated !");
    }


    public static final int NET_WIFI = 1;
    public static final int NET_4G = 4;
    public static final int NET_3G = 3;
    public static final int NET_2G = 2;
    public static final int NET_UNKNOWN = 5;
    public static final int NET_NO = -1;

    /**
     * TelephonyManager.NETWORK_TYPE_GSM = 16
     */
    private static final int NETWORK_TYPE_GSM = 16;
    /**
     * TelephonyManager.NETWORK_TYPE_TD_SCDMA = 17
     */
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    /**
     * TelephonyManager.NETWORK_TYPE_IWLAN = 18
     */
    private static final int NETWORK_TYPE_IWLAN = 18;

    private static int netStatusLast = NET_UNKNOWN;
    private static int netStatusCurrent = NET_UNKNOWN;


    /**
     * Returns details about the currently active default data network.
     *
     * @return a {@link NetworkInfo} object for the current default network
     * or {@code null} if no default network is currently active.
     */
    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
            (ConnectivityManager) Common.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo();
    }


    /**
     * 判断网络是否可用
     */
    public static boolean isAvailable() {

        NetworkInfo networkInfo = getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }


    /**
     * 获取当前的网络类型名称
     *
     * @return NET_WIFI, NET_4G, NET_3G, NET_2G, NET_UNKNOWN, NET_NO
     */
    public static String getConnectedTypeName() {
        switch (getConnectedType()) {
            case NET_WIFI:
                return "NET_WIFI";
            case NET_4G:
                return "NET_4G";
            case NET_3G:
                return "NET_3G";
            case NET_2G:
                return "NET_2G";
            case NET_NO:
                return "NET_NO";
            default:
                return "NET_UNKNOWN";
        }
    }


    /**
     * 获取当前的网络类型名称
     *
     * @return NET_WIFI, NET_4G, NET_3G, NET_2G, NET_UNKNOWN, NET_NO
     */
    public static String getConnectedTypeName(int connectedType) {
        switch (connectedType) {
            case NET_WIFI:
                return "NET_WIFI";
            case NET_4G:
                return "NET_4G";
            case NET_3G:
                return "NET_3G";
            case NET_2G:
                return "NET_2G";
            case NET_NO:
                return "NET_NO";
            default:
                return "NET_UNKNOWN";
        }
    }


    /**
     * 判断是不是WIFI网络
     */
    public static boolean isWIFI() {
        NetworkInfo networkInfo = getActiveNetworkInfo();
        return networkInfo != null
            && networkInfo.isAvailable()
            && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 判断是不是4G网络
     */
    public static boolean is4G() {
        NetworkInfo networkInfo = getActiveNetworkInfo();
        return networkInfo != null
            && networkInfo.isAvailable()
            && networkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }


    /**
     * 获取当前否网络连接类型
     *
     * @return NET_WIFI, NET_4G, NET_3G, NET_2G, NET_UNKNOWN, NET_NO
     */
    public static int getConnectedType() {
        int netType = NET_NO;
        NetworkInfo networkInfo = getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NET_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (networkInfo.getSubtype()) {

                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NET_2G;
                        break;

                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NET_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NET_4G;
                        break;
                    default:

                        String subtypeName = networkInfo.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                            || subtypeName.equalsIgnoreCase("WCDMA")
                            || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NET_3G;
                        } else {
                            netType = NET_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NET_UNKNOWN;
            }
        }
        return netType;
    }


    /**
     * 打开设置界面
     */
    public static void openSetting(Context context) {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        context.startActivity(intent);
    }


    /**
     * 网络状态切换广播接受者
     */
    private static BroadcastReceiver receiver = new BroadcastReceiver() {

        // 这个方法在刚注册广播后会回调一次
        @Override public void onReceive(Context context, Intent intent) {

            // 记录现在的网络状态
            netStatusCurrent = getConnectedType();

            if (mNetChangedListener != null) {
                // 上一个网络状态不是初始值
                if (netStatusLast != NET_UNKNOWN) {

                    // 上个状态是断网
                    if (netStatusLast == NetUtil.NET_NO && netStatusCurrent != NetUtil.NET_NO) {
                        mNetChangedListener.onNetReconnect();
                    }
                }

                if (netStatusCurrent == NetUtil.NET_NO) {
                    mNetChangedListener.onNetBreak();
                }

            }

            // 记录上一次的网络状态
            netStatusLast = netStatusCurrent;
        }
    };

    private static OnNetChangedListener mNetChangedListener;

    public static boolean isRegister() {
        return mNetChangedListener != null;
    }


    public interface OnNetChangedListener {
        void onNetBreak();
        void onNetReconnect();
    }


    /**
     * 注册网络状态切换广播接受者
     */
    public static void registerNetChangedListener(OnNetChangedListener listener) {

        mNetChangedListener = listener;
        // 获取当前网络状态
        netStatusCurrent = getConnectedType();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        Common.getContext().registerReceiver(receiver, filter);
    }


    /**
     * 反注册网络状态切换广播接受者
     */
    public static void unregisterNetChangedListener() {
        netStatusLast = NET_UNKNOWN;
        netStatusCurrent = NET_UNKNOWN;
        mNetChangedListener = null;
        Common.getContext().unregisterReceiver(receiver);
    }
}