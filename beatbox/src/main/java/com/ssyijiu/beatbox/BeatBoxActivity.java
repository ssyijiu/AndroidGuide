package com.ssyijiu.beatbox;

import android.support.v4.app.Fragment;
import com.ssyijiu.beatbox.app.SimpleFragmentActivity;

public class BeatBoxActivity extends SimpleFragmentActivity {

    @Override protected Fragment createFragment() {

        new NoLeakHandler().postDelayed(new Runnable() {
            @Override public void run() {

            }
        }, 500000);
        return BeatBoxFragment.newInstance();
    }
}
