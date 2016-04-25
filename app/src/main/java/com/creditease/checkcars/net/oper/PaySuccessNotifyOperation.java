/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

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
import com.creditease.checkcars.net.oper.bean.PayNotifyBean.PayNotifyObj;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author 子龍
 * @date 2014年10月23日
 * @company 宜信
 */
public final class PaySuccessNotifyOperation extends Oper< Object >
{

    public PaySuccessNotifyOperation(Context mContext, RequestListener listener)
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
        return Config.ROOT_URL + "Pay/addAccountTranfer";
    }

    /**
     * @param status       : 支付状态 PayNotifyBean.STATUS_PAY_*
     * @param tradeNumber  :订单的orderNumber
     * @param sellerId     :支付对象的账号
     * @param tradeAmount: 支付金额
     * @param platform:    支付平台: 0 支付宝
     * @param platform     下午5:56:06
     */
    public void setParam(int status, String tradeNumber, int orderType, String sellerId,
                         String tradeAmount)
    {
        PayNotifyObj obj = new PayNotifyObj(status, tradeNumber, sellerId, tradeAmount, 0, orderType);
        String json = JsonUtils.requestObjectBean(obj);
        String data = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", data);
        setParam(param);
    }

}
