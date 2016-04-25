/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.db.CarInfoUtil;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.bean.CarInfoBeanResult;
import com.creditease.checkcars.net.oper.bean.OperResponseForWeixin;
import com.creditease.checkcars.tools.JsonUtils;
import com.google.gson.JsonSyntaxException;

/**
 * 通过VIN获取车基本信息
 *
 * @author 子龍
 * @function
 * @date 2015年7月6日
 * @company CREDITEASE
 */
public final class GetCarInfoByVinOperation extends Oper< Object >
{
    private String vin = "";
    private Context context;

    public GetCarInfoByVinOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
        context = mContext;
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
        OperResponseForWeixin resp = OperResponseFactory.parseResultForWeixin(result);
        String data = resp.data;
        Bundle bundle = new Bundle();
        if ( !TextUtils.isEmpty(data) )
        {
            try
            {
                CarInfoBeanResult orderResult =
                        JsonUtils.parserJsonStringToObject(data, CarInfoBeanResult.class);
                orderResult.result.vin = vin;
                CarInfoUtil.getUtil(context).saveOrUpdateCarInfo(orderResult.result);
                bundle.putParcelable(BUNDLE_EXTRA_DATA, orderResult.result);
            } catch ( CEDbException e )
            {
                e.printStackTrace();
            } catch ( JsonSyntaxException e )
            {
                throw new CEFailtureException(e.getMessage(), resp);
            }
        }
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.VIN_QUERY_URL + vin;
    }

    /**
     * 设置请求参数
     *
     * @param clientUuid 下午6:13:05
     */
    public void setParam(String vin)
    {
        this.vin = vin;
        setParam(new RequestParam());
    }

}
