package com.ssyijiu.beatbox.bean;

import android.content.res.AssetManager;
import com.ssyijiu.common.Common;
import com.ssyijiu.common.log.MLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssyijiu on 2017/5/12.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class BeatBox {

    private static final String SOUNDS_FOLDER = "sample_sounds";

    private AssetManager assetManager;
    private List<Sound> sounds = new ArrayList<>();


    public BeatBox() {
        assetManager = Common.getContext().getAssets();
        loadSounts();
    }


    private void loadSounts() {
        String[] soundNames;
        try {
            soundNames = assetManager.list(SOUNDS_FOLDER);
            for (String filename : soundNames) {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                sounds.add(sound);
            }
        } catch (IOException e) {
            MLog.e("Could not list assets", e);
        }
    }

    public List<Sound> getSounds() {
        return sounds;
    }
}
