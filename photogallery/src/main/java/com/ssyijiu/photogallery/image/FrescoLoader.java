package com.ssyijiu.photogallery.image;//package com.ssyijiu.picassof.imageloader;

import android.content.Context;
import android.widget.ImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ssyijiu.photogallery.R;

/**
 * Created by ssyijiu on 2016/12/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

class FrescoLoader implements ImageLoader {

    static final FrescoLoader INSTANCE = new FrescoLoader();

    private GenericDraweeHierarchyBuilder hierarchyBuilder;


    private FrescoLoader() {
    }


    @Override public void init(Context context) {
        Fresco.initialize(context);
        hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources());
        hierarchyBuilder.setFailureImage(R.color.colorAccent);
    }


    @Override public void loadImage(String url, ImageView imageView) {
        if (imageView instanceof SimpleDraweeView) {

            SimpleDraweeView draweeView = (SimpleDraweeView) imageView;
            draweeView.setHierarchy(hierarchyBuilder.build());
            draweeView.setImageURI(url);
        }
    }


    @Override public void loadImage(String url, ImageView imageView, ImageOptions options) {
        if (imageView instanceof SimpleDraweeView) {

            SimpleDraweeView draweeView = (SimpleDraweeView) imageView;
            if (options.isHas(options.error())) {
                hierarchyBuilder.setFailureImage(options.error());
            }
            draweeView.setHierarchy(hierarchyBuilder.build());
            draweeView.setImageURI(url);
        }
    }
}
