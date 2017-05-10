package com.ssyijiu.common.util;

import java.io.File;

/**
 * Created by ssyijiu on 2017/5/10.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class FileUtil {

    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (!file.delete()) {
                ToastUtil.show(file.getAbsolutePath() + " delete error !");
            }
        }
    }
}
