package com.creditease.checkcars.net.oper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.base.Result;
import com.creditease.checkcars.net.oper.bean.MineHeadPicBean.MineHeadObj;
import com.creditease.checkcars.tools.JsonUtils;
import com.creditease.checkcars.tools.PhotoUtils;
import com.creditease.checkcars.tools.TimeUtils;
import com.creditease.utilframe.http.client.multipart.MultipartEntity;
import com.creditease.utilframe.http.client.multipart.content.ContentBody;
import com.creditease.utilframe.http.client.multipart.content.FileBody;
import com.creditease.utilframe.http.client.multipart.content.StringBody;

public class UploadHeadPicOperation extends Oper< Object >
{
    private String imagePath;
    private Context mContext;

    public UploadHeadPicOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
        this.mContext = mContext;
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse resp = OperResponseFactory.parseResult(result);
        if ( (resp != null) && resp.result == Result.RESULT_SUCCESS )
        {
            String appraiserId = SharePrefenceManager.getAppraiserId(mContext);
            SharePrefenceManager.setHeadPic(mContext, imagePath, appraiserId);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_MINE_HEAD_PHOTO;
    }

    public void setParam(Bitmap bitmap, String coreId)
    {
        RequestParam param = new RequestParam();
        String json = JsonUtils.requestObjectBean(new MineHeadObj(coreId));
        String imgData = DESEncrypt.encryption(json);
        MultipartEntity entity = new MultipartEntity();
        imagePath =
                PhotoUtils.IMAGE_IMG_PATH + "temp_" + TimeUtils.timeToString(new Date(), "yyyyMMddHHmmss")
                        + ".jpg";
        PhotoUtils.savePhotoToSDCard(bitmap, imagePath);
        File file = new File(imagePath);
        if ( file.exists() )
        {
            ContentBody photoFile = new FileBody(file, "multipart/form-data");
            entity.addPart("imgFile", photoFile);
        }
        try
        {
            entity.addPart("para", new StringBody(imgData));
        } catch ( UnsupportedEncodingException e )
        {

        }
        param.setBodyEntity(entity);
        setParam(param);
    }

}
