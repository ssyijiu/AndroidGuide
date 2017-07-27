package com.ssyijiu.photogallery.recycleradapter;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.ssyijiu.photogallery.R;
import com.ssyijiu.photogallery.bean.MeiZhi;
import com.ssyijiu.photogallery.image.ImageOptions;
import com.ssyijiu.photogallery.image.Vinci;
import java.util.List;

import static com.ssyijiu.photogallery.image.ImageOptions.getOptions;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<MeiZhi.Results> datas;
    private OnRecyclerClickListener onClickListener;


    public PhotoAdapter(List<MeiZhi.Results> datas, OnRecyclerClickListener onClickListener) {
        this.datas = datas;
        this.onClickListener = onClickListener;
    }


    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(
            inflater.inflate(R.layout.item_gallery, parent, false), onClickListener);
    }


    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        ImageOptions options = ImageOptions.getOptions();
        options.error(android.R.color.darker_gray);
        Vinci.instance().loadImage(datas.get(position).url, holder.imageView, options);

        holder.url = datas.get(position).url;
        ViewCompat.setTransitionName(holder.imageView, holder.url);
    }


    @Override public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public String url;


        ViewHolder(View itemView, final OnRecyclerClickListener onRecyclerClickListener) {
            super(itemView);
            imageView = (ImageView) itemView;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (onRecyclerClickListener != null) {
                        onRecyclerClickListener.OnRecyclerClick(ViewHolder.this);
                    }
                }
            });
        }
    }


    public interface OnRecyclerClickListener {
        void OnRecyclerClick(ViewHolder holder);
    }
}
