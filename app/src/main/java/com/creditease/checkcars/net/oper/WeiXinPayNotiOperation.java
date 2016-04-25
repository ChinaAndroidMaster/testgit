/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.bean.OperResponseForWeixin;

/**
 * 微信支付提醒
 *
 * @author 子龍
 * @function
 * @date 2015年3月24日
 * @company CREDITEASE
 */
public final class WeiXinPayNotiOperation extends Oper< Object >
{

    public WeiXinPayNotiOperation(Context mContext, RequestListener listener)
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
        OperResponseForWeixin resp = OperResponseFactory.parseResultForWeixin(result);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WeiXin_URL;
    }

    /**
     * 设置请求参数
     *
     * @param clientUuid 下午6:13:05
     */
    public void setParam(CarOrder order)
    {
        RequestParam param = new RequestParam();
        param.addBodyParameter("ouuid", order.uuid);
        param.addBodyParameter("cuuid", order.customer.uuid);
        param.addBodyParameter("cname", order.customer.nickName);
        param.addBodyParameter("cphone", order.customer.phoneNumber);
        param.addBodyParameter("sfphone", order.appraiser.phoneNumber);
        setParam(param);
    }

}
