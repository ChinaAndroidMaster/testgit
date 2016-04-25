/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import java.io.File;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.ReportImageBean.RequestObj;
import com.creditease.checkcars.tools.ImageUtils;
import com.creditease.checkcars.tools.JsonUtils;
import com.creditease.utilframe.http.client.multipart.MultipartEntity;
import com.creditease.utilframe.http.client.multipart.content.ContentBody;
import com.creditease.utilframe.http.client.multipart.content.FileBody;
import com.creditease.utilframe.http.client.multipart.content.StringBody;

/**
 * 添加报告
 *
 * @author 子龍
 * @function
 * @date 2015年3月13日
 * @company CREDITEASE
 */
public final class ReportImageUpdateOperation extends Oper< Object >
{

    public ReportImageUpdateOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    /**
     * 解析数据
     *
     * @throws CEFailtureException
     * @throws CERequestException
     */
    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse resp = OperResponseFactory.parseResult(result);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.ROOT_URL + "updateReportCarForImage";
    }

    /**
     * 设置请求参数
     *
     * @param imgType   Car.TYPE_IMG_BASE (0车前45度1车后45度2厂牌3发动机舱4中控全貌5仪表盘6后备箱7基本信息封面图)
     * @param imagePath
     * @param uuid      订单ID 下午6:13:05
     */
    public void setParam(int imgType, String imagePath, String uuid)
    {
        RequestParam param = new RequestParam();
        RequestObj obj = new RequestObj(uuid, imgType);
        String json = JsonUtils.requestObjectBean(obj);
        String userData = DESEncrypt.encryption(json);
        MultipartEntity entity = new MultipartEntity();
        param.setBodyEntity(entity);
        imagePath = ImageUtils.saveImage(mContext, imagePath);
        File file = new File(imagePath);
        if ( file.exists() )
        {
            ContentBody photoFile = new FileBody(file, "multipart/form-data");
            entity.addPart("imgFile", photoFile);
        }
        try
        {
            entity.addPart("para", new StringBody(userData));
        } catch ( UnsupportedEncodingException e )
        {

        }
        param.setBodyEntity(entity);
        setParam(param);
    }

}
