package com.ssyijiu.beatbox.bean;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
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
    private static final int MAX_SOUNDS = 5;

    private AssetManager assetManager;
    private List<Sound> sounds = new ArrayList<>();
    private SoundPool soundPool;


    public BeatBox() {
        assetManager = Common.getContext().getAssets();
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(MAX_SOUNDS);
        builder.setAudioAttributes(
            new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build());
        soundPool = builder.build();
        loadSounts();
    }


    private void loadSounts() {
        String[] soundNames;
        try {
            soundNames = assetManager.list(SOUNDS_FOLDER);
            for (String filename : soundNames) {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                sounds.add(sound);
            }
        } catch (IOException e) {
            MLog.e("Could not list assets", e);
        }
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = assetManager.openFd(sound.getAssetPath());
        // 加载音频，准备播放，加载失败返回 null
        Integer soundId = soundPool.load(afd, 1);
        // 记录下 id
        sound.setSoundId(soundId);
    }


    public List<Sound> getSounds() {
        return sounds;
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if(soundId == null) {
            return;
        }

        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        soundPool.release();
    }
}
