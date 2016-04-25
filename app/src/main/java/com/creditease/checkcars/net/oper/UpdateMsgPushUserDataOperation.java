/**
 * Copyright © 2015 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.MsgPushUserBean;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author 子龍
 * @function
 * @date 2015年7月20日
 * @company CREDITEASE
 */
public final class UpdateMsgPushUserDataOperation extends Oper< Object >
{
    public UpdateMsgPushUserDataOperation(Context mContext, RequestListener listener)
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
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, res);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_UPDATE_MSGPUSH_USERDATA;
    }

    /**
     * 设置登录参数
     *
     * @param phoneNumber 电话
     * @param password    密码 上午10:44:36
     */
    public void setParam(String tag, String alias, String aliasType, String deviceToken)
    {
        String uuid = SharePrefenceManager.getUserId(mContext);
        String json =
                JsonUtils
                        .requestObjectBean(new MsgPushUserBean(uuid, alias, aliasType, deviceToken, tag, 0));
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

}
