package com.ssyijiu.common.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import com.ssyijiu.common.Common;

/**
 * Created by ssyijiu on 2017/2/16.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class ClipBoardUtil {

    private ClipBoardUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("ClipBoardUtil cannot be instantiated !");
    }


    static void copyToClipBoard(String text) {
        ClipData clipData = ClipData.newPlainText("common_copy", text);
        ClipboardManager manager =
            (ClipboardManager) Common.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
    }
}
