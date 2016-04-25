/**
 * Copyright © 2013 Credit Ease. All rights reserved.
 */
package com.creditease.checkcars;

import android.annotation.TargetApi;
import android.app.Application;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;

import com.creditease.checkcars.msgpush.MessageHandler;
import com.creditease.checkcars.msgpush.MsgPushManager;
import com.creditease.checkcars.net.oper.base.OperHandler;
import com.creditease.checkcars.tools.PhotoUtils;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.volley.ImageCacheManager;
import com.creditease.checkcars.ui.volley.RequestManager;

/**
 * @author 子龍
 * @function
 * @date 2015年5月19日
 * @company CREDITEASE
 */
public class BaseApplication extends Application
{
    public static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 100;
    public static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
    public static int DISK_IMAGECACHE_QUALITY = 80;

    /************************
     * 定位
     **********************************/
    public String city;
    public String area;


    /**
     * Create the image cache. Uses Memory Cache by default. Change to Disk for a Disk based LRU
     * implementation.
     */
    private void createImageCache()
    {
        ImageCacheManager.getInstance().init(this, getPackageCodePath(), DISK_IMAGECACHE_SIZE,
                DISK_IMAGECACHE_COMPRESS_FORMAT, DISK_IMAGECACHE_QUALITY, ImageCacheManager.CacheType.DISK);
    }

    /**
     * Intialize the request manager and the image cache
     */
    private void init()
    {
        // init oper handler
        OperHandler.initOperHandler();
        PhotoUtils.createFileDir();
        RequestManager.init(this);
        createImageCache();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
        // 初始化消息推送
        MessageHandler.createHandler(this);
        MsgPushManager.getManager(this).initMSGPush();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        // Log.e("BaseApplication", "onLowMemory");
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        // Log.e("BaseApplication", "onTerminate");
    }

    @TargetApi( Build.VERSION_CODES.ICE_CREAM_SANDWICH )
    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        // Log.e("BaseApplication", "onTrimMemory");
    }

    public void showToast(String text)
    {
        ZLToast.getToast().showToast(this, text);
    }
}
