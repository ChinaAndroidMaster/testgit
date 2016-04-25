package com.creditease.checkcars.net.uploadimg;

import java.io.File;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.utilframe.db.annotation.Table;
import com.creditease.utilframe.exception.HttpException;

/**
 * 图片上传实体类
 *
 * @author 子龍
 * @date 2014年12月21日
 * @company 宜信
 */
@Table( name = "uploadimg_tab" )
public class UploadImage implements RequestListener
{
    // TODO 加上 上行条件（与服务器报告同步先决条件）

    public int id;
    public int venderId;
    public String uploadId;

    public String imagePath;

    /******************
     * 上传图片operation
     ************************/
    public String uploadUrl;

    public String key;

    public String value;

    public boolean isNeedEncrypt;

    public boolean isUpload = false;
    /******************************************/

    public ImageUploadOperation uploadOper;

    private Context context;

    public UploadImage()
    {
    }

    public UploadImage(String imagePath, String uploadUrl, String key, String value,
                       boolean isNeedEncrypt)
    {
        super();
        uploadId = System.currentTimeMillis() + "";
        this.imagePath = imagePath;
        this.uploadUrl = uploadUrl;
        this.key = key;
        this.value = value;
        this.isNeedEncrypt = isNeedEncrypt;
    }

    public UploadImage(String uploadId, String imagePath, String uploadUrl, String key, String value,
                       boolean isNeedEncrypt)
    {
        super();
        this.uploadId = uploadId;
        this.imagePath = imagePath;
        this.uploadUrl = uploadUrl;
        this.key = key;
        this.value = value;
        this.isNeedEncrypt = isNeedEncrypt;
    }

    @Override
    public boolean equals(Object o)
    {
        if ( o instanceof UploadImage )
        {
            UploadImage i = ( UploadImage ) o;
            return uploadId.equals(i.uploadId);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    public boolean isImageExist()
    {
        if ( imagePath == null )
        {
            return false;
        }
        File file = new File(imagePath);
        return file.exists() && (file.length() > 100);
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        isUpload = false;
        ImageUploadManager.getManager(context).onDataError(errorMsg, result);
    }

    @Override
    public void onFailure(OperResponse response)
    {
        isUpload = false;
        ImageUploadManager.getManager(context).onFailure(response);
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        isUpload = false;
        ImageUploadManager.getManager(context).onRequestError(error, msg);
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        isUpload = false;
        ImageUploadManager.getManager(context).onSuccess(bundle);
    }

    public void setUploadOper(Context mContext, String url, String key, String value,
                              boolean isNeedEncrypt)
    {
        context = mContext;
        uploadOper = new ImageUploadOperation(mContext, url, this);
        uploadOper.setParam(key, value, isNeedEncrypt, this);
    }

    public void setUploadOper(Context mContext, String url, String key, String value,
                              boolean isNeedEncrypt, UploadProgressListener listener)
    {
        context = mContext;
        uploadOper = new ImageUploadOperation(mContext, url, listener);
        uploadOper.setParam(key, value, isNeedEncrypt, this);
    }

    /**
     * @return boolean 是否启动下载线程
     */
    public boolean upload()
    {
        if ( (uploadOper != null) && !isUpload )
        {
            isUpload = true;
            uploadOper.execute();
            return true;
        }
        return false;
    }

}
