package com.ssyijiu.photogallery.http;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.NetUtil;
import com.ssyijiu.common.util.ResourceUtil;
import com.ssyijiu.photogallery.PhotoGalleryActivity;
import com.ssyijiu.photogallery.R;
import com.ssyijiu.photogallery.app.App;
import com.ssyijiu.photogallery.bean.MeiZhi;
import com.ssyijiu.photogallery.tools.Preferences;

/**
 * Created by ssyijiu on 2017/7/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PollService extends IntentService {
    private static final int POLL_INTERVAL = 1000 * 60; // 60 s
    public PollService() {
        super("PollService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MLog.i(System.currentTimeMillis());
        MLog.i(Thread.currentThread().getName() + intent);
        if (NetUtil.isAvailable()) {
            String result = new HttpGet().getUrlString(Host.meizhi_url + "1");
            MeiZhi meiZhi = Gsons.json2Bean(result, MeiZhi.class);
            String lastUrl = Preferences.loadLastMeizhi();
            if (meiZhi != null && meiZhi.results != null && meiZhi.results.size() > 0) {
                String newUrl = meiZhi.results.get(0).url;
                if (lastUrl.equals(newUrl)) {
                    MLog.i("Get a old girl : " + lastUrl);
                } else {
                    MLog.i("Get a new girl : " + newUrl);
                    Preferences.saveLastMeizhi(newUrl);
                }
            }
            // 通知
            Intent i = PhotoGalleryActivity.newIntent(this);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)   // 小图标
                    .setContentTitle(ResourceUtil.getString(R.string.new_pictures_title))  // 标题
                    .setContentText(ResourceUtil.getString(R.string.new_pictures_text))    // 文字
                    .setContentIntent(pendingIntent)   // 点击的 Intent
                    .setAutoCancel(true)               // 点击后通知消失
                    .build();
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
            // 发送通知的 id，取消通知时用到，id 相同下一条通知会顶掉上一条
            notificationManager.notify(0, notification);
        }
    }

    public static Intent newIntent() {
        return new Intent(App.getContext(), PollService.class);
    }

    public static void setServiceAlarm(boolean isOn) {
        Intent intent = PollService.newIntent();
        PendingIntent pendingIntent = PendingIntent.getService(App.getContext(), 0, intent, 0);
        // 获取定时管理器
        AlarmManager alarmManager = (AlarmManager) App.getContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (isOn) {
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL, pendingIntent);
            } else {
                // 取消定时器
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }

    public static boolean isServiceAlarmOn() {
        Intent intent = PollService.newIntent();
        PendingIntent pendingIntent = PendingIntent.getService(App.getContext(), 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }
}
