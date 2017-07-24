package com.ssyijiu.photogallery.image;

import android.content.Context;
import android.util.AttributeSet;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by ssyijiu on 2016/12/24.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

// public class ImageVinci extends AppCompatImageView {
//     public ImageVinci(Context context) {
//         super(context);
//     }
//
//
//     public ImageVinci(Context context, AttributeSet attrs) {
//         super(context, attrs);
//     }
//
//
//     public ImageVinci(Context context, AttributeSet attrs, int defStyleAttr) {
//         super(context, attrs, defStyleAttr);
//     }
// }

// Fresco

public class ImageVinci extends SimpleDraweeView {

   public ImageVinci(Context context) {
       super(context);
   }

   public ImageVinci(Context context, AttributeSet attrs) {
       super(context, attrs);
   }

   public ImageVinci(Context context, AttributeSet attrs, int defStyle) {
       super(context, attrs, defStyle);
   }

   public ImageVinci(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
       super(context, attrs, defStyleAttr, defStyleRes);
   }

   public ImageVinci(Context context, GenericDraweeHierarchy hierarchy) {
       super(context, hierarchy);
   }
}
