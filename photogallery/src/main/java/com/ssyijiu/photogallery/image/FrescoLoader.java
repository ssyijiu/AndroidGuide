package com.ssyijiu.photogallery.image;

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

    private GenericDraweeHierarchyBuilder defaultHierarchyBuilder;


    private FrescoLoader() {
    }


    @Override public void init(Context context) {
        Fresco.initialize(context);
        defaultHierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources());
        setHierarchyBuilder(defaultHierarchyBuilder);

    }


    @Override public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, null);
    }


    @Override public void loadImage(String url, ImageView imageView, ImageOptions options) {
        if (imageView instanceof SimpleDraweeView) {
            SimpleDraweeView draweeView = (SimpleDraweeView) imageView;

            if (options != null) {
                if (options.set(options.error())) {
                    defaultHierarchyBuilder.setFailureImage(options.error());
                }
            }

            draweeView.setHierarchy(defaultHierarchyBuilder.build());
            draweeView.setImageURI(url);
            resetHierarchyBuilder(defaultHierarchyBuilder);
        }
    }


    private void resetHierarchyBuilder(GenericDraweeHierarchyBuilder builder) {
        builder.reset();
        setHierarchyBuilder(builder);
    }

    private void setHierarchyBuilder(GenericDraweeHierarchyBuilder builder) {
        builder.setFailureImage(R.color.colorAccent);
    }
}
