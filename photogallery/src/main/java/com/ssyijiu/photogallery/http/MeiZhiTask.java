package com.ssyijiu.photogallery.http;

import android.os.AsyncTask;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.photogallery.bean.MeiZhi;
import java.io.IOException;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public abstract class MeiZhiTask extends AsyncTask<Integer, Void, MeiZhi> {

    // Integer doInBackground 的参数, AsyncTask.execute 的参数
    // Integer 用于更新进度，在 doInBackground 使用 publishProgress 发送进度
    //         在 onProgressUpdate 接收进度更新 UI
    // MeiZhi doInBackground 的返回 onPostExecute 参数，后台线程的操作结果

    @Override protected MeiZhi doInBackground(Integer... params) {

        MeiZhi meiZhi = null;

        try {
            String result = new HttpUtil().getUrlString(Host.meizhi_url + params[0]);
            meiZhi = Gsons.json2Bean(result, MeiZhi.class);
        } catch (IOException e) {
            MLog.e("Failed to fetch URL: ", e);
        }
        return meiZhi;
    }


    @Override protected void onPostExecute(MeiZhi meiZhi) {
        super.onPostExecute(meiZhi);
        if(meiZhi != null && !meiZhi.error) {
            afterMeiZhi(meiZhi);
        }
    }



    protected abstract void afterMeiZhi(MeiZhi meiZhi);
}
