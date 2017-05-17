package com.ssyijiu.photogallery.recycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ssyijiu.photogallery.R;
import com.ssyijiu.photogallery.bean.MeiZhi;
import java.util.List;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<MeiZhi.Results> datas;


    public PhotoAdapter(List<MeiZhi.Results> datas) {
        this.datas = datas;
    }


    @Override public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new PhotoHolder(
            inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
    }


    @Override public void onBindViewHolder(PhotoHolder holder, int position) {
        holder.bindMeiZhi(datas.get(position));
    }


    @Override public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    class PhotoHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView;
        }

        public void bindMeiZhi(MeiZhi.Results meiZhi) {
            titleTextView.setText(meiZhi.desc);
        }
    }
}
