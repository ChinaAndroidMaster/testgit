package com.creditease.checkcars.net.uploadimg;

import java.io.File;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.uploadimg.CredetEaseMultipartEntity.ProgressListener;
import com.creditease.checkcars.tools.ImageUtils;
import com.creditease.utilframe.http.client.multipart.content.ContentBody;
import com.creditease.utilframe.http.client.multipart.content.FileBody;
import com.creditease.utilframe.http.client.multipart.content.StringBody;

/**
 * 图片上传网络业务
 *
 * @author 子龍
 * @date 2014年12月21日
 * @company 宜信
 */
public class ImageUploadOperation extends Oper< Object >
{
    public static final String BUNDLE_EXTRA_OPER_ID = "_oper_id";
    public String url;
    public String key;
    public String value;
    public boolean isNeedEncrypt;
    private UploadImage image;
    private UploadProgressListener listener;
    private long fileSize;

    public ImageUploadOperation(Context mContext, String url)
    {
        super(mContext, ImageUploadManager.getManager(mContext));
        this.url = url;
    }

    public ImageUploadOperation(Context mContext, String url, RequestListener listener)
    {
        super(mContext, listener);
        this.url = url;
    }

    public ImageUploadOperation(Context mContext, String url, UploadProgressListener listener)
    {
        super(mContext, ImageUploadManager.getManager(mContext));
        this.url = url;
        this.listener = listener;
    }

    public ImageUploadOperation(Context mContext, String url,
                                UploadProgressListener progressListener, RequestListener listener)
    {
        super(mContext, listener);
        this.url = url;
        this.listener = progressListener;
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        image.isUpload = false;
        OperResponse res = OperResponseFactory.parseResult(result);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_DATA, res);
        bundle.putString(BUNDLE_EXTRA_OPER_ID, image.uploadId);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return url;
    }

    public void setParam(String key, String value, boolean isNeedEncrypt, final UploadImage image)
    {
        this.image = image;
        this.key = key;
        this.value = value;
        this.isNeedEncrypt = isNeedEncrypt;

        RequestParam param = new RequestParam();
        if ( isNeedEncrypt && !TextUtils.isEmpty(value) )
        {
            value = DESEncrypt.encryption(value);
        }
        CredetEaseMultipartEntity entity = new CredetEaseMultipartEntity(new ProgressListener()
        {

            @Override
            public void transferred(final long num)
            {
                if ( mContext instanceof Activity )
                {
                    (( Activity ) mContext).runOnUiThread(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            if ( listener != null )
                            {
                                listener.onProgress(image.uploadId, fileSize, num);
                            }
                        }
                    });
                }
            }
        });
        param.setBodyEntity(entity);
        String uid = SharePrefenceManager.getAppraiserId(mContext) + "_";
        // 避免 重复压缩
        if ( image.imagePath.contains("temp_") || !image.imagePath.contains(uid) )
        {
            image.imagePath = ImageUtils.saveImage(mContext, image.imagePath);
        }
        if ( !TextUtils.isEmpty(image.imagePath) )
        {
            File file = new File(image.imagePath);
            if ( file.exists() )
            {
                ContentBody photoFile = new FileBody(file, "multipart/form-data");
                entity.addPart("imgFile", photoFile);
            }
        }
        if ( !TextUtils.isEmpty(key) && !TextUtils.isEmpty(value) )
        {
            try
            {
                entity.addPart(key, new StringBody(value));
            } catch ( UnsupportedEncodingException e )
            {

            }
        }
        fileSize = entity.getContentLength();
        param.setBodyEntity(entity);
        setParam(param);
    }

}
