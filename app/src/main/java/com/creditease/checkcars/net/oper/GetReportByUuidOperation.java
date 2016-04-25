/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.CCClassify;
import com.creditease.checkcars.data.bean.CCItem;
import com.creditease.checkcars.data.bean.Car;
import com.creditease.checkcars.data.bean.CarImg;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.bean.Note;
import com.creditease.checkcars.data.db.CarReportUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.ReportGetBean;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * 添加报告
 *
 * @author 子龍
 * @function
 * @date 2015年3月13日
 * @company CREDITEASE
 */
public final class GetReportByUuidOperation extends Oper< Object >
{

    public String uuid;

    public GetReportByUuidOperation(Context mContext, RequestListener listener)
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
        String data = DESEncrypt.decryption(resp.data);
        CarReport carReport = JsonUtils.parserJsonStringToObject(data, CarReport.class);
        if ( (carReport != null) && (carReport.imgList != null) && (carReport.imgList.size() > 0) )
        {
            initReportCar(carReport.reportCar, carReport.imgList);
        }
        if ( (carReport != null) && (carReport.cRoot != null) )
        {
            initAnswers(carReport.cRoot);
        }
        // 保存
        try
        {
            if ( carReport != null )
            {
                CarReportUtil.getUtil(mContext).saveOrUpdateReport(carReport);
            }
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        bundle.putParcelable(BUNDLE_EXTRA_DATA, carReport);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_USERS_REPORT_GET_URL;
    }

    private void initAnswers(Note note)
    {
        String reportStr = SharePrefenceManager.getBaseReport(mContext);
        final CarReport cr = JsonUtils.parserJsonStringToObject(reportStr, CarReport.class);
        ArrayList< CCClassify > ncList = note.children;
        ArrayList< CCClassify > cList = cr.cRoot.children;
        for ( int i = 0, len = cList.size(); i < len; i++ )
        {
            CCClassify ccClassify = cList.get(i);
            CCClassify nccClassify = ncList.get(i);
            for ( int j = 0, l = ccClassify.attrs.size(); j < l; j++ )
            {
                if ( (j >= ccClassify.attrs.size()) || (j >= nccClassify.attrs.size()) )
                {
                    continue;
                }
                CCItem ccItem = ccClassify.attrs.get(j);
                CCItem nccItem = nccClassify.attrs.get(j);
                nccItem.answerList = ccItem.answerList;
            }
        }
    }


    private void initReportCar(Car reportCar, ArrayList< CarImg > imgList)
    {
        int index = imgList.indexOf(new CarImg(CarImg.TYPE_IMG_FRONT));
        if ( index != -1 )
        {
            CarImg img = imgList.get(index);
            img.imgName = CarImg.getName(mContext, CarImg.TYPE_IMG_FRONT);
            reportCar.imgList.set(0, img);
            index = -1;
        }
        index = imgList.indexOf(new CarImg(CarImg.TYPE_IMG_BACK));
        if ( index != -1 )
        {
            CarImg img = imgList.get(index);
            img.imgName = CarImg.getName(mContext, CarImg.TYPE_IMG_BACK);
            reportCar.imgList.set(1, img);
            index = -1;
        }
        // index = imgList.indexOf(new CarImg(CarImg.TYPE_IMG_BASE));
        // if (index != -1) {
        // CarImg img = imgList.get(index);
        // img.imgName = CarImg.getName(mContext, CarImg.TYPE_IMG_BASE);
        // reportCar.imgList.set(1, img);
        // index = -1;
        // }
        index = imgList.indexOf(new CarImg(CarImg.TYPE_IMG_BRAND));
        if ( index != -1 )
        {
            CarImg img = imgList.get(index);
            img.imgName = CarImg.getName(mContext, CarImg.TYPE_IMG_BRAND);
            reportCar.imgList.set(2, img);
            index = -1;
        }
        index = imgList.indexOf(new CarImg(CarImg.TYPE_IMG_ENGINE));
        if ( index != -1 )
        {
            CarImg img = imgList.get(index);
            img.imgName = CarImg.getName(mContext, CarImg.TYPE_IMG_ENGINE);
            reportCar.imgList.set(3, img);
            index = -1;
        }
        index = imgList.indexOf(new CarImg(CarImg.TYPE_IMG_CCR));
        if ( index != -1 )
        {
            CarImg img = imgList.get(index);
            img.imgName = CarImg.getName(mContext, CarImg.TYPE_IMG_CCR);
            reportCar.imgList.set(4, img);
            index = -1;
        }
        index = imgList.indexOf(new CarImg(CarImg.TYPE_IMG_PANEL));
        if ( index != -1 )
        {
            CarImg img = imgList.get(index);
            img.imgName = CarImg.getName(mContext, CarImg.TYPE_IMG_PANEL);
            reportCar.imgList.set(5, img);
            index = -1;
        }
        index = imgList.indexOf(new CarImg(CarImg.TYPE_IMG_TRUNK));
        if ( index != -1 )
        {
            CarImg img = imgList.get(index);
            img.imgName = CarImg.getName(mContext, CarImg.TYPE_IMG_TRUNK);
            reportCar.imgList.set(6, img);
            index = -1;
        }
        index = imgList.indexOf(new CarImg(CarImg.TYPE_IMG_UNDERPAN));
        if ( index != -1 )
        {
            CarImg img = imgList.get(index);
            img.imgName = CarImg.getName(mContext, CarImg.TYPE_IMG_UNDERPAN);
            reportCar.imgList.set(7, img);
            index = -1;
        }

    }

    /**
     * 设置请求参数
     *
     * @param uuid report uuid 订单ID 下午6:13:05
     */
    public void setParam(String uuid)
    {
        this.uuid = uuid;
        // RequestObj obj = new RequestObj(uuid, ReportGetBean.TYPE_APPRAISER);
        // String json = JsonUtils.requestObjectBean(obj);
        // String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("uuid", DESEncrypt.encryption(uuid));
        param.addBodyParameter("toType", ReportGetBean.TYPE_APPRAISER + "");
        setParam(param);
    }

}
