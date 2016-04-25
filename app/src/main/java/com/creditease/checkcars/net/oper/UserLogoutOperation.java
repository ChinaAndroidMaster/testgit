/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;

/**
 * @author 子龍
 * @date 2014年10月24日
 * @company 宜信
 */
public final class UserLogoutOperation extends Oper< Object >
{

    public UserLogoutOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse res = OperResponseFactory.parseResult(result);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_DATA, res);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_USERS_USER_LOGOUT_URL;
    }

    /**
     * 设置登出参数
     *
     * @param userJson
     */
    public void setParam()
    {
        // TODO
        RequestParam param = new RequestParam();
        // param.addBodyParameter(Config.WS_REQUEST_TYPE_USER_ID, venderId);
        setParam(param);
    }

}
