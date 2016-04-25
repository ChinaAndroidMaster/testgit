package com.creditease.checkcars.net.uploadimg;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * 图片上传工厂
 *
 * @author 子龍
 * @date 2014年12月21日
 * @company 宜信
 */
public class UploadImageFactory
{

    public static List< UploadImage > build(ArrayList< String > imagePaths, Context mContext, String url,
                                            String key, String value, boolean isNeedEncrypt)
    {
        List< UploadImage > list = new ArrayList< UploadImage >();
        for ( String path : imagePaths )
        {
            list.add(build(path, mContext, url, key, value, isNeedEncrypt));
        }
        return list;
    }

    /**
     * 工厂uploadImage
     *
     * @param imagePath     图片地址
     * @param mContext
     * @param url           协议接口地址
     * @param key           上行数据key
     * @param value         上行数据value
     * @param isNeedEncrypt 是否需要加密
     * @return
     */
    public static UploadImage build(String imagePath, Context mContext, String url, String key,
                                    String value, boolean isNeedEncrypt)
    {
        UploadImage image = new UploadImage(imagePath, url, key, value, isNeedEncrypt);
        return image;
    }

    /**
     * 工厂uploadImage
     *
     * @param imagePath     图片地址
     * @param mContext
     * @param url           协议接口地址
     * @param key           上行数据key
     * @param value         上行数据value
     * @param isNeedEncrypt 是否需要加密
     * @param listener      上传进度条
     * @return
     */
    public static UploadImage build(String imagePath, Context mContext, String url, String key,
                                    String value, boolean isNeedEncrypt, UploadProgressListener listener)
    {
        UploadImage image = new UploadImage(imagePath, url, key, value, isNeedEncrypt);
        return image;
    }

}
