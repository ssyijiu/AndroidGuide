package com.ssyijiu.photogallery.image;

import android.content.Context;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.ssyijiu.photogallery.R;

/**
 * Created by ssyijiu on 2017/7/23.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public class PicassoLoader implements ImageLoader {

    static final PicassoLoader INSTANCE = new PicassoLoader();

    private Context context;


    private PicassoLoader() {
    }


    @Override public void init(Context context) {
        this.context = context;
    }


    @Override public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, null);
    }


    @Override public void loadImage(String url, ImageView imageView, ImageOptions options) {
        RequestCreator request = picasso(url);
        if (options != null) {
            if (options.set(options.error())) {
                request.error(options.error());
            }
        }
        request.into(imageView);
    }


    /**
     * 获取默认的 RequestCreator
     */
    private RequestCreator picasso(String url) {
        return Picasso.with(context)
            .load(url)
            .error(R.color.colorPrimary)
            .priority(Picasso.Priority.HIGH);
    }
}
