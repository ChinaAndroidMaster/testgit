package com.creditease.checkcars.tools;

import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.View;

import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.utilframe.BitmapUtils;

public class ImageUtils
{
    private static ImageUtils utils;

    private static final long MAX_MEM_SIZE_UPLOAD_IMAGE = 150 * 1000 * 3;

    public static ImageUtils getUtils(Context context)
    {
        if ( utils == null )
        {
            utils = new ImageUtils(context);
        }
        return utils;
    }

    public static int readPictureDegree(String path)
    {
        int degree = 0;
        try
        {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch ( orientation )
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
        return degree;
    }

    public static String saveImage(Context mContext, String imagePath)
    {
        String savePath = null;
        String fileName =
                SharePrefenceManager.getAppraiserId(mContext) + "_"
                        + TimeUtils.timeToString(new Date(), "yyyyMMddHHmmss") + ".jpg";
        Bitmap bitmap = PhotoUtils.getBitmapFromFile(imagePath, MAX_MEM_SIZE_UPLOAD_IMAGE);
        if ( bitmap == null )
        {
            return imagePath;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(readPictureDegree(imagePath));
        Bitmap bitmap2 = null;
        try
        {
            bitmap2 =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if ( bitmap2 != null )
            {
                String path = PhotoUtils.savePhotoToSDCard(bitmap2, PhotoUtils.IMAGE_IMG_PATH + fileName);
                if ( path != null )
                {
                    savePath = path;
                }
            }
        } catch ( OutOfMemoryError e )
        {
            e.printStackTrace();
        } finally
        {
            if ( (bitmap2 != null) && !bitmap2.isRecycled() )
            {
                bitmap2.recycle();
            }
            bitmap2 = null;
            if ( (bitmap != null) && !bitmap.isRecycled() )
            {
                bitmap.recycle();
            }
            bitmap = null;
        }

        return savePath;
    }

    private BitmapUtils bu;

    private ImageUtils(Context context)
    {
        bu = new BitmapUtils(context);
    }

    public void clearCache()
    {
        bu.clearCache();
    }

    public void clearDiskCache()
    {
        bu.clearDiskCache();
    }

    public void clearMemoryCache()
    {
        bu.clearMemoryCache();
    }

    /**
     * 配置加载失败图片
     *
     * @param resId 上午10:11:07
     */
    public void configDefaultLoadFailedImage(int resId)
    {
        bu.configDefaultLoadFailedImage(resId);
    }

    /**
     * 配置加载中图片
     *
     * @param resId 上午10:11:10
     */
    public void configDefaultLoadingImage(int resId)
    {
        bu.configDefaultLoadingImage(resId);
    }

    /**
     * 展示
     *
     * @param container
     * @param uri       上午10:11:46
     */
    public < T extends View > void display(T container, String uri)
    {
        bu.display(container, uri);
    }

}
