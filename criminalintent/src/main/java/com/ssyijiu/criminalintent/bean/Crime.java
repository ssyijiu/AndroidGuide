package com.ssyijiu.criminalintent.bean;

import android.text.TextUtils;
import com.ssyijiu.common.util.DateUtil;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by ssyijiu on 2017/4/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class Crime extends RealmObject implements Serializable{

    @PrimaryKey public String id;

    public String title;
    public Date date;
    public boolean solved;
    public String suspect;
    public String suspectPhoneNum;


    public Crime() {
        id = UUID.randomUUID().toString();
        date = new Date();
    }


    public String getDate() {
        return DateUtil.date2String(date,
            new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.getDefault()));
    }

    public boolean couldDelete() {
        return TextUtils.isEmpty(title);
    }

    public boolean hasSuspect() {
        return !TextUtils.isEmpty(suspect);
    }

    public boolean hasSuspectPhoneNum() {
        return !TextUtils.isEmpty(suspectPhoneNum);
    }

    public String getPhotoFilename() {
        return "IMG_" + id + ".jpg";
    }
}
