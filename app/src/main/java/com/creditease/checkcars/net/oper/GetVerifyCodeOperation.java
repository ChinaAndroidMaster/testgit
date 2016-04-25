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
import com.creditease.checkcars.net.oper.bean.VerifyBean;
import com.creditease.checkcars.net.oper.bean.VerifyBean.ResultObj;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author 子龍
 * @function
 * @date 2015年8月3日
 * @company CREDITEASE
 */
public final class GetVerifyCodeOperation extends Oper< Object >
{

    /**
     * 使用验证码的业务类型
     */
    public static final int TYPE_VERIFY_CODE_PWD = 0;// 设置密码类型

    public GetVerifyCodeOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse res = OperResponseFactory.parseResult(result);
        String data = DESEncrypt.decryption(res.data);
        ResultObj obj = JsonUtils.parserJsonStringToObject(data, ResultObj.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_EXTRA_DATA, obj.code);
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, res);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_USERS_GET_VERIFY_CODE_URL;
    }

    public void setParam(String phoneNumber, int type)
    {
        String json = JsonUtils.requestObjectBean(new VerifyBean.VerifyObj(phoneNumber, type));
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

}
