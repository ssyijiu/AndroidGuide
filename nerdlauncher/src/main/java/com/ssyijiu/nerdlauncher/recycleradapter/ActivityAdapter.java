package com.ssyijiu.nerdlauncher.recycleradapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ssyijiu.common.util.BitmapUtil;
import com.ssyijiu.common.util.DensityUtil;
import com.ssyijiu.nerdlauncher.app.App;
import java.util.List;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder> {

    private List<ResolveInfo> datas;
    private Activity context;


    public ActivityAdapter(List<ResolveInfo> datas, Activity context) {
        this.datas = datas;
        this.context = context;
    }


    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ActivityHolder(
            inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
    }


    @Override public void onBindViewHolder(ActivityHolder holder, int position) {
        ResolveInfo resolveInfo = datas.get(position);
        holder.bindActivity(resolveInfo);
    }


    @Override public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ResolveInfo resolveInfo;
        private TextView nameTextView;


        ActivityHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView;
            itemView.setOnClickListener(this);
        }


        void bindActivity(ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
            PackageManager pm = App.getContext().getPackageManager();
            String appName = this.resolveInfo.loadLabel(pm).toString();
            nameTextView.setText(appName);

            Drawable icon = this.resolveInfo.loadIcon(pm);
            BitmapUtil.setTextDrawableLeft(nameTextView, icon, DensityUtil.dp2px(10));
        }


        @Override public void onClick(View v) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;

            // Intent.ACTION_MAIN
            // 同样的 Activity 可能会显示不同的用户界面，明确启动意图
            Intent i = new Intent(Intent.ACTION_MAIN)
                .setClassName(activityInfo.applicationInfo.packageName,
                    activityInfo.name);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }
    }
}
