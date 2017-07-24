package com.ssyijiu.photogallery.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ssyijiu on 2017/5/18.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class ImageLoader<T> extends HandlerThread {

    private static final String TAG = "ImageLoader";
    private static final int MESSAGE_DOWNLOAD = 0;

    // 子线程 Handler
    private Handler mRequestHandler;
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();

    // 主线程 Handler
    private Handler nMainHandler;
    private LoadFinishListener<T> mLoadFinishListener;


    public interface LoadFinishListener<T> {
        void onImageLoadFinish(T target, Bitmap bitmap);
    }


    public void setImageLoadListener(LoadFinishListener<T> listener) {
        mLoadFinishListener = listener;
    }


    private boolean hasQuit;


    public ImageLoader(Handler mainHandler) {
        super(TAG);
        nMainHandler = mainHandler;
    }


    @Override public boolean quit() {
        hasQuit = true;
        return super.quit();
    }


    // 分发下载任务
    public void queueImage(T obj, String url) {
        if (url == null) {
            mRequestMap.remove(obj);
        } else {

            mRequestMap.put(obj, url);

            // 创建一个消息
            // target: mRequestHandler
            // what: MESSAGE_DOWNLOAD
            // obj: obj (ViewHolder)
            Message msg = mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, obj);

            // handler.sendMessage(msg)
            msg.sendToTarget();
        }
    }


    /**
     * 这个方法在子线程执行
     */
    @Override
    protected void onLooperPrepared() {
        // Looper 准备好后
        // 创建 Handler
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }


    private void handleRequest(final T target) {
        final String url = mRequestMap.get(target);

        if (url == null) {
            return;
        }

        // 子线程下载图片
        byte[] bitmapBytes = new HttpUtil().getUrlBytes(url);

        if(bitmapBytes == null) return;
        final Bitmap bitmap = BitmapFactory
            .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

        // 主线程刷新 UI
        nMainHandler.post(new Runnable() {
            @Override public void run() {

                if (!mRequestMap.get(target).equals(url)
                    || hasQuit) {
                    return;
                }

                mRequestMap.remove(target);
                if (mLoadFinishListener != null) {
                    mLoadFinishListener.onImageLoadFinish(target, bitmap);
                }
            }
        });
    }


    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }
}
