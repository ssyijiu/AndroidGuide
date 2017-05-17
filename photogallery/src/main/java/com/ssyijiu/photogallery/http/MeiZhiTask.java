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

public abstract class MeiZhiTask extends AsyncTask<Void, Void, MeiZhi> {

    @Override protected MeiZhi doInBackground(Void... params) {

        MeiZhi meiZhi = null;

        try {
            String result = new HttpUtil().getUrlString(Host.host);
            MLog.i("Fetched contents of URL: " + result);
            meiZhi = Gsons.json2Bean(result, MeiZhi.class);
        } catch (IOException e) {
            MLog.e("Failed to fetch URL: ", e);
        }
        return meiZhi;
    }


    @Override protected void onPostExecute(MeiZhi meiZhi) {
        super.onPostExecute(meiZhi);
        afterGetMeiZhi(meiZhi);
    }


    protected abstract void afterGetMeiZhi(MeiZhi meiZhi);
}
