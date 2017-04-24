package com.ssyijiu.criminalintent.bean;

import com.ssyijiu.common.util.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by ssyijiu on 2017/4/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class Crime {
    public UUID id;
    public String title;
    public Date date;
    public boolean solved;

    public Crime() {
        id = UUID.randomUUID();
        date = new Date();
    }

    public String getDate() {
        return DateUtil.date2String(date,
            new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.getDefault()));
    }
}
