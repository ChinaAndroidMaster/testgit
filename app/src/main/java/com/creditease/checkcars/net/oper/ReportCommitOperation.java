/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.db.CarReportUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.ReportCommitBean.RequestObj;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * 提交报告
 *
 * @author 子龍
 * @function
 * @date 2015年3月13日
 * @company CREDITEASE
 */
public final class ReportCommitOperation extends Oper< Object >
{

    private CarReport report;

    public ReportCommitOperation(Context mContext, RequestListener listener)
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
        // 保存
        report.status = CarReport.STATUS_FINISH;
        try
        {
            CarReportUtil.getUtil(mContext).updateReport(report);
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_USERS_REPORT_UPDATE_URL;
    }

    /**
     * 设置请求参数
     *
     * @param uuid 下午6:13:05
     */
    public void setParam(CarReport report)
    {
        this.report = report;
        report.status = CarReport.STATUS_FINISH;
        RequestObj obj = new RequestObj(report);
        String json = JsonUtils.requestObjectBean(obj);
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

}
