package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.db.CarOrderUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.CarPreReportBean;
import com.creditease.checkcars.tools.JsonUtils;

public class GetPreCarReportOperation extends Oper< Object >
{


    public GetPreCarReportOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }


    public void setParam()
    {
        String appraiserId = SharePrefenceManager.getAppraiserId(mContext);
        String json = JsonUtils.requestObjectBean(new CarPreReportBean.CarPreRepostObj(appraiserId));
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse resp = OperResponseFactory.parseResult(result);
        String data = DESEncrypt.decryption(resp.data);
        CarOrder objResult = JsonUtils.parserJsonStringToObject(data, CarOrder.class);
        if ( objResult != null )
        {
            objResult.appraiserId = objResult.appraiser.uuid;
            objResult.appraiserJson = JsonUtils.requestObjectBean(objResult.appraiser);
            objResult.reportListJson = JsonUtils.requestObjectBean(objResult.reportList);
            try
            {
                CarOrderUtil.getUtil(mContext).saveOrUpdateOrder(objResult, objResult.uuid);
            } catch ( CEDbException e )
            {
                e.printStackTrace();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        bundle.putParcelable(BUNDLE_EXTRA_DATA, objResult);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_GET_PRE_CARREPORT;
    }

}
