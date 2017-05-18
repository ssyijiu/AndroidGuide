package com.ssyijiu.photogallery.recycleradapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.ssyijiu.common.util.ResUtil;
import com.ssyijiu.photogallery.R;
import com.ssyijiu.photogallery.bean.MeiZhi;
import com.ssyijiu.photogallery.http.ImageLoader;
import java.util.List;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<MeiZhi.Results> datas;
    private ImageLoader<PhotoHolder> imageLoader;
    private Activity activity;


    public PhotoAdapter(List<MeiZhi.Results> datas, ImageLoader<PhotoHolder> imageLoader) {
        this.datas = datas;
        this.imageLoader = imageLoader;
    }


    @Override public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.activity = (Activity) parent.getContext();
        return new PhotoHolder(
            inflater.inflate(R.layout.item_gallery, parent, false));
    }


    @Override public void onBindViewHolder(PhotoHolder holder, int position) {
        // imageLoader.queueImage(holder, datas.get(position).url);
        Glide.with(activity).load(datas.get(position).url)
            .into(holder.photoView);

    }


    @Override public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public static class PhotoHolder extends RecyclerView.ViewHolder {

        public ImageView photoView;


        public PhotoHolder(View itemView) {
            super(itemView);
            photoView = (ImageView) itemView;
        }


        public void bindMeiZhi(Drawable drawable) {
            photoView.setImageDrawable(drawable);
        }
    }
}
