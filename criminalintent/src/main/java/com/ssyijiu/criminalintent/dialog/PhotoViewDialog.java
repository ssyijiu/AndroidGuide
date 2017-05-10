package com.ssyijiu.criminalintent.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.ssyijiu.common.util.BitmapUtil;
import com.ssyijiu.criminalintent.R;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by ssyijiu on 2017/5/10.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

@Deprecated
public class PhotoViewDialog extends BaseDialog {

    private static final String ARGS_PHOTO_PATH = "args_photo_path";


    public static PhotoViewDialog newInstance(String photoPath) {
        Bundle args = new Bundle();
        PhotoViewDialog fragment = new PhotoViewDialog();
        args.putString(ARGS_PHOTO_PATH, photoPath);
        fragment.setArguments(args);
        return fragment;
    }


    // 设置 Dialog 全屏
    // public void onActivityCreated(Bundle savedInstanceState) {
    //     Window window = getDialog().getWindow();
    //     if(window != null) {
    //         window.requestFeature(Window.FEATURE_NO_TITLE);
    //     }
    //     super.onActivityCreated(savedInstanceState);
    //
    //     if(window != null) {
    //         window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    //         window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
    //             WindowManager.LayoutParams.MATCH_PARENT);
    //     }
    //
    // }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        PhotoView photoView = (PhotoView) View.inflate(context, R.layout.dialog_photo_view, null);

        String path = getArguments().getString(ARGS_PHOTO_PATH);
        photoView.setImageBitmap(BitmapUtil.getScaledBitmap(path, context));

        builder.setView(photoView);

        return builder.create();
    }
}
