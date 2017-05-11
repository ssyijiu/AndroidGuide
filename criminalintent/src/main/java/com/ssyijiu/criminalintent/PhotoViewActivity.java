package com.ssyijiu.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import com.ssyijiu.criminalintent.app.BaseActivity;
import com.ssyijiu.criminalintent.util.image.Vinci;
import com.wingsofts.dragphotoview.DragPhotoView;

/**
 * Created by ssyijiu on 2017/5/10.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class PhotoViewActivity extends BaseActivity {

    private static final String EXTRA_PHOTO_PATH = "extra_photo_path";
    @BindView(R.id.photo_view) DragPhotoView photoView;
    @BindView(R.id.photo_view_root) FrameLayout photoViewRoot;
    private String path;


    @Override protected int getLayoutResId() {
        hideStatusBar();
        return R.layout.activity_photo_view;
    }


    @Override protected void parseIntent(Intent intent) {
        super.parseIntent(intent);
        path = intent.getStringExtra(EXTRA_PHOTO_PATH);
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        photoView.setOnExitListener(new DragPhotoView.OnExitListener() {
            @Override
            public void onExit(DragPhotoView dragPhotoView, float v, float v1, float v2, float v3) {
                onBackPressed();
            }
        });

        Vinci.instance().loadImage(context, path, photoView);
    }


    public static void start(Context context, String path, ActivityOptionsCompat optionsCompat) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra(EXTRA_PHOTO_PATH, path);
        context.startActivity(intent, optionsCompat.toBundle());
    }


    /**
     * 隐藏 ActionBar
     * 并为 StatusBar 设置颜色
     */
    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.BLACK);
            getWindow().setStatusBarColor(Color.BLACK);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

}
