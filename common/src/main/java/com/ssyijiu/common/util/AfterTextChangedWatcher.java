package com.ssyijiu.common.util;

import android.text.TextWatcher;

/**
 * @author lxm created on 2017/11/23
 */

public abstract class AfterTextChangedWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
