package com.ssyijiu.photogallery.http;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.NetUtil;
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


    @Override protected void onHandleIntent(@Nullable Intent intent) {
        MLog.i(Thread.currentThread().getName() + intent);

        if(NetUtil.isAvailable()) {
            String result = new HttpUtil().getUrlString(Host.meizhi_url + "1");
            MeiZhi meiZhi = Gsons.json2Bean(result,MeiZhi.class);

            String lastUrl = Preferences.loadLastMeizhi();

            if(meiZhi.results != null && meiZhi.results.size() > 0) {
                String newUrl = meiZhi.results.get(0).url;
                if(lastUrl.equals(newUrl)) {
                    MLog.i("Get a old girl : " + lastUrl);
                } else {
                    MLog.i("Get a new girl : " + newUrl);
                    Preferences.saveLastMeizhi(newUrl);
                }
            }

        }



    }

    public static Intent newIntent() {
        return new Intent(App.getContext(), PollService.class);
    }

    public static void start() {
        App.getContext().startService(newIntent());
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = PollService.newIntent();

        // Context context, int requestCode, Intent intent, int flags
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);

        // 获取定时管理器
        AlarmManager alarmManager = (AlarmManager)
            context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            // 设置定时器
            // type：时间类型 ELAPSED_REALTIME(系统时钟)
            // triggerAtMillis：触发事件，与上面类型对应
            // intervalMillis：时间间隔
            // PendingIntent
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            // 取消定时器
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
}
