package com.ssyijiu.beatbox.recycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ssyijiu.beatbox.R;
import com.ssyijiu.beatbox.bean.Sound;
import com.ssyijiu.common.log.MLog;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ssyijiu on 2017/5/12.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundHolder> {

    private List<Sound> sounds;

    public SoundAdapter(List<Sound> sounds) {
        this.sounds = sounds;
    }

    @Override public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new SoundHolder(layoutInflater
            .inflate(R.layout.item_sound, parent, false));
    }


    @Override public void onBindViewHolder(SoundHolder holder, int position) {
        Sound sound = sounds.get(position);
        holder.bindSound(sound);
    }


    @Override public int getItemCount() {
        return sounds.size();
    }


    class SoundHolder extends RecyclerView.ViewHolder {

        private Button button;
        private Sound sound;

        public SoundHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.item_sound_button);
        }

        public void bindSound(Sound sound) {
            this.sound = sound;
            button.setText(sound.getName());
        }
    }
}