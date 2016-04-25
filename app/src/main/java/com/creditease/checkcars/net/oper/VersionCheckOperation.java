/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.NewVersion;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.VersionBean;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author 子龍
 * @function
 * @date 2015年4月24日
 * @company CREDITEASE
 */
public final class VersionCheckOperation extends Oper< Object >
{

    public VersionCheckOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse res = OperResponseFactory.parseResult(result);
        String data = DESEncrypt.decryption(res.data);
        NewVersion obj = JsonUtils.parserJsonStringToObject(data, NewVersion.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_DATA, obj);
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, res);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_VERSION_CHECK_URL;
    }

    public void setParam(String appName, String versionName)
    {
        String json = JsonUtils.requestObjectBean(new VersionBean.VersionObj(appName, versionName));
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

}
