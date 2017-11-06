package com.bignerdranch.android.photogallery;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    //атрибут what для Message.
    private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit = false;

    //Handler для сообщений
    private Handler mRequestHandler;
    //Потокобезопасный HashMap, очередь сообщений
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();

    //Передаваемый хэндлер, для вызова коллбэка по выполнению загрузки
    // фотографии
    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;

    //Callback по загрузке фотографии
    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, Bitmap bitmap);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    //Вызывается перед проверкой looper'ом череди.
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            //будет вызываться когда message будет извленчено из queue и
            // готово к исполнению.
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

    //Добавить message в очередь на загрузку.
    public void queueThumbnail(T photoHolder, String url) {
        Log.i(TAG, "Got a URL: " + url);

        //Добавление объектов в очередь на выполнение
        if (url == null) {
            mRequestMap.remove(photoHolder);
        } else {
            mRequestMap.put(photoHolder, url);
            //obtainMessage создаст сообщение в списке без дублирования.
            //sendToTarget отправка сообщения в Looper и размещение в конце
            // очереди. target автоматически станет = mRequestHandler.
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, photoHolder)
                    .sendToTarget();
        }
    }

    //Выполнение работы по сообщению (загузка фотографии).
    private void handleRequest(final T target) {
        try {
            final String url = mRequestMap.get(target);

            if (url == null) {
                return;
            }

            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            mResponseHandler.post(new Runnable() {
                public void run() {
                    if (mRequestMap.get(target) != url ||
                            mHasQuit) {
                        return;
                    }

                    mRequestMap.remove(target);
                    mThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
                }
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRequestMap.clear();
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }
}
