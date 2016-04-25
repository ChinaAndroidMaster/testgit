package com.creditease.checkcars.net.uploadimg;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.utilframe.exception.HttpException;

/**
 * 图片上传管理
 *
 * @author 子龍
 * @date 2014年12月21日
 * @company 宜信
 */
public class ImageUploadManager implements Runnable, RequestListener
{
    private final static int THREAD_MAX_SIZE = 5;// 最大并发线程数
    private static ImageUploadManager manager = null;
    private static Thread uploadThread = null;
    private static LinkedHashMap< String, UploadImage > uploadImagePool =
            new LinkedHashMap< String, UploadImage >();
    private static ArrayList< String > keyPool = new ArrayList< String >();// key 池
    private Context context;
    private String userId;// 商户Id
    private UploadImageDBUtil dbUtil;
    private boolean loop = false;
    private int thread_size = 0;// 并发线程数
    private boolean isExcute = false;

    private ImageUploadManager(Context context)
    {
        this.context = context;
        userId = SharePrefenceManager.getAppraiserId(context);
        loop = true;
        dbUtil = UploadImageDBUtil.getDBUtil(context);
    }

    /**
     * 获取图片上传管理器
     *
     * @param context
     * @return
     */
    public synchronized static ImageUploadManager getManager(Context context)
    {
        if ( manager == null )
        {
            manager = new ImageUploadManager(context);
        }
        return manager;
    }

    /**
     * 添加
     *
     * @param imageList
     * @throws CEDbException
     */
    public void addUploadImage(List< UploadImage > imageList) throws CEDbException
    {
        for ( UploadImage uploadImage : imageList )
        {
            addUploadImage(uploadImage);
        }
        wakeUp();
    }

    /**
     * 添加
     *
     * @param image
     * @throws CEDbException
     */
    public void addUploadImage(UploadImage image) throws CEDbException
    {
        String key = image.uploadId;
        if ( !uploadImagePool.containsKey(key) )
        {
            uploadImagePool.put(key, image);
            keyPool.add(key);
            dbUtil.saveOrUpdateUploadImage(image);
        }
        wakeUp();
    }

    /**
     * 上传
     */
    private void excute()
    {
        UploadImage image = getImage();
        if ( (image != null) && image.isImageExist() )
        {
            image.setUploadOper(context, image.uploadUrl, image.key, image.value, image.isNeedEncrypt);
            if ( image.upload() )
            {
                isExcute = true;
                thread_size++;
            }
        }
    }

    /*
     * 从容器中拿上传图片
     */
    private UploadImage getImage()
    {
        if ( keyPool.size() == 0 )
        {
            return null;
        }
        String key = keyPool.get(0);
        if ( uploadImagePool.containsKey(key) )
        {
            UploadImage image = uploadImagePool.get(key);
            if ( image.isUpload )
            {
                return null;
            }
            // 将key移动至队尾
            if ( !TextUtils.isEmpty(image.imagePath) )
            {
                keyPool.remove(0);
                keyPool.add(key);
                return image;
            } else
            {
                uploadImagePool.remove(key);
                keyPool.remove(0);
                try
                {
                    dbUtil.deleteUploadImage(userId, key);
                } catch ( CEDbException e )
                {
                }
            }
        } else
        {
            // 将key删除
            uploadImagePool.remove(key);
            keyPool.remove(0);
            try
            {
                dbUtil.deleteUploadImage(userId, key);
            } catch ( CEDbException e )
            {
            }
        }
        return null;
    }

    private synchronized void initOrStartUploadThread(Context context)
    {
        if ( (uploadThread == null) || !uploadThread.isAlive() )
        {
            uploadThread = new Thread(this);
            uploadThread.start();
        }
    }

    /*
     * 初始化图片池
     */
    private void initPool()
    {
        try
        {
            // 数据库取数据
            List< UploadImage > list = dbUtil.getUploadImageList(userId);
            // 封装业务
            if ( (list != null) && (list.size() > 0) )
            {
                uploadImagePool.clear();
                addUploadImage(list);
            }
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        // ZLToast.getToast().showToast(context, "失败");
        isExcute = false;
        releaseAndWakeUp();
    }

    @Override
    public void onFailure(OperResponse response)
    {
        // ZLToast.getToast().showToast(context, "失败");
        isExcute = false;
        releaseAndWakeUp();
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        // ZLToast.getToast().showToast(context, "失败");
        isExcute = false;
        releaseAndWakeUp();
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        // ZLToast.getToast().showToast(context, "上传成功");
        isExcute = false;
        releaseAndWakeUp();
        String id = bundle.getString(ImageUploadOperation.BUNDLE_EXTRA_OPER_ID);
        removeUploadImage(id);
    }

    private void releaseAndWakeUp()
    {
        thread_size--;
        if ( thread_size < 0 )
        {
            thread_size = 0;
        }
        wakeUp();
    }

    /**
     * @param uploadIdList 下午7:15:02
     */
    public void removeUploadImage(List< String > uploadIdList)
    {
        for ( String uploadId : uploadIdList )
        {
            String key = uploadId;
            if ( uploadImagePool.containsKey(key) )
            {
                uploadImagePool.remove(key);
                keyPool.remove(key);
                try
                {
                    dbUtil.deleteUploadImage(userId, key);
                } catch ( CEDbException e )
                {
                }
            }
        }
    }

    public void removeUploadImage(String uploadId)
    {
        String key = uploadId;
        if ( uploadImagePool.containsKey(key) )
        {
            uploadImagePool.remove(key);
            keyPool.remove(key);
            try
            {
                dbUtil.deleteUploadImage(userId, key);
            } catch ( CEDbException e )
            {
            }
        }
    }

    @Override
    public void run()
    {
        initPool();
        while ( loop )
        {
            if ( uploadImagePool.isEmpty() || (keyPool.size() == 0) || (thread_size > THREAD_MAX_SIZE)
                    || isExcute )
            {
                LockSupport.park();
            } else
            {
                excute();
            }

            try
            {
                Thread.sleep(1500);
            } catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动上传服务
     */
    public void startService(Context context)
    {
        loop = true;
        initOrStartUploadThread(context);
        wakeUp();
    }

    /**
     * 停止服务
     */
    public void stopService()
    {
        wakeUp();
        loop = false;
    }

    private void wakeUp()
    {
        LockSupport.unpark(uploadThread);
    }

}
