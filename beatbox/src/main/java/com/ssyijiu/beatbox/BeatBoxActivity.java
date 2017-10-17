package com.ssyijiu.beatbox;

import android.support.v4.app.Fragment;
import com.ssyijiu.beatbox.app.AbstractSimpleFragmentActivity;

public class BeatBoxActivity extends AbstractSimpleFragmentActivity {

    @Override protected Fragment createFragment() {

        return BeatBoxFragment.newInstance();
    }
}
