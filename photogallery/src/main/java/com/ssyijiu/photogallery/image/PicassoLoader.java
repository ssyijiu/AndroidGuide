package com.ssyijiu.photogallery.image;

import android.content.Context;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.ssyijiu.photogallery.R;

/**
 * Created by ssyijiu on 2017/7/23.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public class PicassoLoader implements ImageLoader {

    private PicassoLoader() {
    }


    static final PicassoLoader INSTANCE = new PicassoLoader();

    @Override public void loadImage(Context context, String url, ImageView imageView) {
        Picasso.with(context)
            .load(url)
            .error(R.color.colorPrimary)
            .into(imageView);
    }

}
