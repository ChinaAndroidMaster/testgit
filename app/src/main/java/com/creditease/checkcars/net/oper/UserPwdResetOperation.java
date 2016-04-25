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
import com.creditease.checkcars.net.oper.bean.PwdResetBean;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author 子龍
 * @date 2014年10月24日
 * @company 宜信
 */
public final class UserPwdResetOperation extends Oper< Object >
{

    public UserPwdResetOperation(Context mContext, RequestListener listener)
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
        return Config.ROOT_URL + "Appraiser/changePassword";
    }

    /**
     * 设置重置密码参数
     *
     * @param userJson
     */
    public void setParam(String phoneNumber, String newPassword)
    {
        String json = JsonUtils.requestObjectBean(new PwdResetBean(phoneNumber, newPassword));
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

}
