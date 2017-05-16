package com.ssyijiu.nerdlauncher;

import android.support.v4.app.Fragment;
import com.ssyijiu.nerdlauncher.app.SimpleFragmentActivity;

public class NerdLauncherActivity extends SimpleFragmentActivity {

    @Override protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
