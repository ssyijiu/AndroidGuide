package com.ssyijiu.photogallery.http;

import android.os.AsyncTask;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.photogallery.bean.MeiZhi;
import java.io.IOException;

/**
 * Created by ssyijiu on 2017/7/26.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public abstract class SearchTask extends AsyncTask<String, Void, MeiZhi> {
    @Override protected MeiZhi doInBackground(String... params) {

        MeiZhi meiZhi = null;

        try {
            String url = Host.search_host + params[0] + Host.search_url + params[1];
            String result = new HttpUtil().getUrlString(url);
            meiZhi = Gsons.json2Bean(result, MeiZhi.class);
        } catch (IOException e) {
            MLog.e("Failed to fetch URL: ", e);
        }
        return meiZhi;
    }


    @Override protected void onPostExecute(MeiZhi meiZhi) {
        super.onPostExecute(meiZhi);
        if (meiZhi != null && !meiZhi.error) {
            afterSearch(meiZhi);
        }
    }


    protected abstract void afterSearch(MeiZhi meiZhi);
}
