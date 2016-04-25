/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.UserCore;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperResponse;

/**
 * @author 子龍
 * @date 2014年10月24日
 * @company 宜信
 */
public final class UserRegisterOperation extends Oper< Object >
{

    public UserRegisterOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        // TODO
        OperResponse res = OperResponseFactory.parseResult(result);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_DATA, res);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_USERS_USER_REGISTER_URL;
    }

    public void setParam(UserCore user)
    {
        // TODO
        // this.user = user;
        // String userData = user.toJson();
        // userData = DESEncrypt.encryption(userData);
        // RequestParam param = new RequestParam();
        // param.addBodyParameter(Config.WS_REQUEST_TYPE_USER_JSON, userData);
        // setParam(param);
    }

}
