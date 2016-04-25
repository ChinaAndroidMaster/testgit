/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.db.BaseReportUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.BaseReportUpdateBean;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * 数据字典更新
 *
 * @author 子龍
 * @function
 * @date 2015年4月24日
 * @company CREDITEASE
 */
public final class BaseReportUpdateOperation extends Oper< Object >
{

    public BaseReportUpdateOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse res = OperResponseFactory.parseResult(result);
        String data = DESEncrypt.decryption(res.data);
        // CarReport report = JsonUtils.parserJsonStringToObject(data,
        // CarReport.class);
        BaseReportUtil.getBRUtil(mContext).saveOrUpdateBaseReport(mContext, data);
        Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_USERS_BASE_REPORT_UPDATE_URL;
    }

    public void setParam()
    {
        String appraiserId = SharePrefenceManager.getAppraiserId(mContext);
        String updateTime = SharePrefenceManager.getBaseReportUpdateTime(mContext, appraiserId);
        if ( TextUtils.isEmpty(updateTime) )
        {
            updateTime = "0";
        }
        String json = JsonUtils.requestObjectBean(new BaseReportUpdateBean(updateTime));
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

}
