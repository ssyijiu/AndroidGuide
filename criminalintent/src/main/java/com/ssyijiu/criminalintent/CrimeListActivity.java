package com.ssyijiu.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.DeviceUtil;
import com.ssyijiu.criminalintent.app.SimpleFragmentActivity;

/**
 * Created by ssyijiu on 2017/4/21.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class CrimeListActivity extends SimpleFragmentActivity {

    @Override protected Fragment createFragment() {
        return new CrimeListFragment();
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        MLog.i(DeviceUtil.getUniqueId());
    }
}
