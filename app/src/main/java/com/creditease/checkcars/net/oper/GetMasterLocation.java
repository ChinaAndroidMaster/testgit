/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.UserPositionInfo;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.LocationBean;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author zgb
 * @date 2014年10月23日
 * @company 宜信
 */
public final class GetMasterLocation extends Oper< Object >
{

    public GetMasterLocation(Context mContext, RequestListener listener)
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
        return Config.WS_GET_LOCATION;
    }

    /**
     * @param toId
     * @param toType
     * @param fromType
     * @param fromId
     * @param latitude
     * @param longitude
     * @param addressName
     */
    public void setParam(UserPositionInfo info)
    {

        String json =
                JsonUtils.requestObjectBean(new LocationBean.LocationObj(info));
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

}
