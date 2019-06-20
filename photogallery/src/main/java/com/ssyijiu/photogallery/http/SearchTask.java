package com.ssyijiu.photogallery.http;

import android.os.AsyncTask;

import com.ssyijiu.photogallery.bean.MeiZhi;

/**
 * Created by ssyijiu on 2017/7/26.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public abstract class SearchTask extends AsyncTask<String, Void, MeiZhi> {
    @Override
    protected MeiZhi doInBackground(String... params) {
        MeiZhi meiZhi = null;
        String url = Host.search_host + params[0] + Host.search_url + params[1];
        String result = new HttpGet().getUrlString(url);
        meiZhi = Gsons.json2Bean(result, MeiZhi.class);
        return meiZhi;
    }

    @Override
    protected void onPostExecute(MeiZhi meiZhi) {
        super.onPostExecute(meiZhi);
        if (meiZhi != null && !meiZhi.error) {
            afterSearch(meiZhi);
        }
    }

    protected abstract void afterSearch(MeiZhi meiZhi);
}
