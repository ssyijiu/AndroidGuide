package com.ssyijiu.criminalintent.dialog;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

/**
 * Created by ssyijiu on 2017/5/10.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class BaseDialog extends DialogFragment {

    protected Activity context;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }
}
