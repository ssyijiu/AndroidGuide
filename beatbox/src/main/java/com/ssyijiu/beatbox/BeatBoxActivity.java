package com.ssyijiu.beatbox;

import android.support.v4.app.Fragment;
import com.ssyijiu.beatbox.app.SimpleFragmentActivity;

public class BeatBoxActivity extends SimpleFragmentActivity {

    @Override protected Fragment createFragment() {
        return BeatBoxFragment.newInstance();
    }
}
