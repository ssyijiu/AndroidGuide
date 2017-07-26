package com.ssyijiu.photogallery.recycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.ssyijiu.photogallery.R;
import com.ssyijiu.photogallery.app.App;
import com.ssyijiu.photogallery.bean.MeiZhi;
import com.ssyijiu.photogallery.image.Vinci;
import java.util.List;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<MeiZhi.Results> datas;


    public PhotoAdapter(List<MeiZhi.Results> datas) {
        this.datas = datas;
    }


    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(
            inflater.inflate(R.layout.item_gallery, parent, false));
    }


    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Vinci.instance().loadImage(App.getContext(),datas.get(position).url,holder.photoView);

    }


    @Override public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView photoView;


        public ViewHolder(View itemView) {
            super(itemView);
            photoView = (ImageView) itemView;
        }
    }
}
