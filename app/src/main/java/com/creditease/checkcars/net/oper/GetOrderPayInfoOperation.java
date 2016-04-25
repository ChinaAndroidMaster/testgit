/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.db.CarOrderUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.OrderPayInfoBean;
import com.creditease.checkcars.net.oper.bean.OrderPayInfoBean.PayInfoResult;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author 子龍
 * @date 2014年10月23日
 * @company 宜信
 */
public final class GetOrderPayInfoOperation extends Oper< Object >
{

    private CarOrder order;

    public GetOrderPayInfoOperation(Context mContext, RequestListener listener)
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
        OperResponse res = OperResponseFactory.parseResult(result);
        String data = DESEncrypt.decryption(res.data);
        PayInfoResult re = JsonUtils.parserJsonStringToObject(data, PayInfoResult.class);
        if ( re != null )
        {
            re.orderId = order.uuid;
            order.tradeAmount = re.tradeAmount + "";
            try
            {
                CarOrderUtil.getUtil(mContext).updateOrder(order);
            } catch ( CEDbException e )
            {
            }
        }
        // Log.i("data", data);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, res);
        bundle.putSerializable(BUNDLE_EXTRA_DATA, re);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_USERS_CARORDER_PAYINFO_URL;
    }

    /**
     * 设置登录参数
     *
     * @param phoneNumber 电话
     * @param password    密码 上午10:44:36
     */
    public void setParam(CarOrder order)
    {
        this.order = order;
        if ( order != null )
        {
            String json = JsonUtils.requestObjectBean(new OrderPayInfoBean.RequestObj(order.uuid));
            String userData = DESEncrypt.encryption(json);
            RequestParam param = new RequestParam();
            param.addBodyParameter("para", userData);
            setParam(param);
        }
    }

}
