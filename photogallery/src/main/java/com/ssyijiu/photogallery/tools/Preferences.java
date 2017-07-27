package com.ssyijiu.photogallery.tools;

import com.ssyijiu.common.util.SPUtil;

/**
 * Created by ssyijiu on 2017/7/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class Preferences {

    private static final String SP_QUERY_KEY = "sp_key_query_key";
    private static final String SP_LAST_MEIZHI = "sp_last_meizhi";

    public static void saveQueryKey(String queryKey) {
        SPUtil.putString(SP_QUERY_KEY, queryKey);
    }

    public static String loadQueryKey() {
        return SPUtil.getString(SP_QUERY_KEY, "");
    }

    public static void saveLastMeizhi(String url) {
        SPUtil.putString(SP_LAST_MEIZHI, url);
    }

    public static String loadLastMeizhi() {
        return SPUtil.getString(SP_LAST_MEIZHI, "");
    }

}
