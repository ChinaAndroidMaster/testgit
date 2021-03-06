package com.creditease.checkcars.net.oper;

import java.util.List;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.OrderEvent;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.tools.JsonUtils;

public class OrderEventOperation extends Oper< Object >
{


    public OrderEventOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
        this.mContext = mContext;
    }

    public void setParam(List< OrderEvent > events)
    {
        String json =
                JsonUtils.requestObjectBean(events);
        String data = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", data);
        setParam(param);
    }

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
        return Config.WS_TIME_RECORDNODE;
    }

}
