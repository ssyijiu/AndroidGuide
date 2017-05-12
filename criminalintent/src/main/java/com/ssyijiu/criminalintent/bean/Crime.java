package com.ssyijiu.criminalintent.bean;

import android.text.TextUtils;
import com.ssyijiu.common.util.DateUtil;
import com.ssyijiu.common.util.ToastUtil;
import com.ssyijiu.criminalintent.app.App;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.File;
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

public class Crime extends RealmObject implements Serializable {

    @PrimaryKey public String id;

    public String title;
    public Date date;
    public boolean solved;
    public String suspect;
    public String suspectPhoneNum;
    public String photoPath;
    public boolean selected;


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


    public boolean hasPhotoFile() {
        return getPhotoFile() != null && getPhotoFile().exists();
    }


    public File getPhotoFile() {
        // 默认没有照片
        if(TextUtils.isEmpty(photoPath)) {
           return null;
        }

        File file = new File(photoPath);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                return null;
            }
        }

        return file;
    }

    /**
     * 获取图片临时存储路径，每次都不一样
     */
    private String getTempPhotoPath() {
        File externalFilesDir = new File(App.getContext()
            .getExternalFilesDir(null), "crime_images");

        return externalFilesDir.getAbsolutePath()
            + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg";
    }

    /**
     * 获取图片临时文件
     */
    public File getTempPhotoFile() {
        File file = new File(getTempPhotoPath());

        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                return null;
            }
        }
        return file;
    }
}
